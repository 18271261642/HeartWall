package com.jkcq.hrwtv.permission_manage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.jkcq.hrwtv.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


/**
 * Created by xueming_wu on 2017/12/4 0004.
 *
 * @author xueming_wu
 */
public class PermissionManageUtil {
    Context mContext;
    private OnGetPermissionListener permissionListener;

    /**
     * 监听进过设置里边修改权限
     *
     * @param setPermissionListener
     */
    public void setSetPermissionListener(OnSetPermissionListener setPermissionListener) {
        this.setPermissionListener = setPermissionListener;
    }

    private OnSetPermissionListener setPermissionListener;

    public PermissionManageUtil(Context context) {
        mContext = context;
    }

    private static final String TAG = "MainActivity";

    /**
     * @param rxPermission
     * @param
     * @param desc               对于请求该权限的描述
     * @param permissionListener
     */
    public void requestPermissions(RxPermissions rxPermission, final String permissions, final String desc,
                                   final OnGetPermissionListener permissionListener) {
        //如果版本是低于6.0 那么则不需要运行时申请权限 直接走有权限的逻辑
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionListener.onGetPermissionYes();
            return;
        } else {
            //如果是可以申请运行时的权限的时候，申请权限的内容没有的情况下 则返回申请权限没有获取到
            if (TextUtils.isEmpty(permissions)) {
                permissionListener.onGetPermissionNo();
                return;
            }
        }
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            processingResults(permissions, true, permissionListener, desc);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            if (permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                                // 用户拒绝了该权限，由于是必须要的权限 那么必须要弹窗提醒
                                processingResults(permissions, true, permissionListener, desc);
                            } else {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                processingResults(permissions, false, permissionListener, desc);
                            }
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            processingResults(permissions, true, permissionListener, desc);
                        }
                    }
                });

    }

    /**
     * 以权限组的形式申请，不需要写描述
     *
     * @param rxPermission
     * @param permissions
     * @param permissionListener
     */
    public void requestPermissionsGroup(RxPermissions rxPermission, String[] permissions,
                                        OnGetPermissionListener permissionListener) {
        //如果版本是低于6.0 那么则不需要运行时申请权限 直接走有权限的逻辑
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionListener.onGetPermissionYes();
            return;
        } else {
            //如果是可以申请运行时的权限的时候，申请权限的内容没有的情况下 则返回申请权限没有获取到
            if (permissions.length == 0) {
                permissionListener.onGetPermissionNo();
                return;
            }
        }
        String desc = "";
        if (permissions == PermissionGroup.STORAGE) {
            desc = mContext.getResources().getString(R.string.permission_storage);
        } else if (permissions == PermissionGroup.CONTACTS) {
            desc = mContext.getResources().getString(R.string.permission_contacts);
        } else if (permissions == PermissionGroup.LOCATION) {
            desc = mContext.getResources().getString(R.string.permission_location);
        } else if (permissions == PermissionGroup.PHONE) {
            desc = mContext.getResources().getString(R.string.permission_phone);
        } else if (permissions == PermissionGroup.CAMERA) {
            desc = mContext.getResources().getString(R.string.permission_photograph);
        } else if (permissions == PermissionGroup.MICROPHONE) {
            desc = mContext.getResources().getString(R.string.permission_recording);
        } else if (permissions == PermissionGroup.SHORTCUT) {
            desc = mContext.getResources().getString(R.string.permission_shortcut);
        }
        requestPermissionsGroup(rxPermission, permissions, desc, permissionListener);
    }

    /**
     * 预定义权限组申请
     *
     * @param rxPermission
     * @param
     * @param permissionListener
     */
    private void requestPermissionsGroup(RxPermissions rxPermission, final String[] permissions, final String desc,
                                         final OnGetPermissionListener permissionListener) {
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permissions[permissions.length - 1].equals(permission.name)) {
                            if (permission.granted) {
                                // 用户已经同意该权限
                                processingResults(permissions, true, permissionListener, desc);
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                //如果是一定要给的权限 比如说是存储权限 那么不给就一定要弹框
                                if (permissions == PermissionGroup.STORAGE) {
                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                    processingResults(permissions, true, permissionListener, desc);
                                } else {
                                    processingResults(permissions, false, permissionListener, desc);

                                }
                            } else {
                                // 用户拒绝了该权限，并且选中『不再询问』
                                processingResults(permissions, true, permissionListener, desc);
                            }
                        }
                    }
                });
    }

    /**
     * 处理获取权限返回数据
     *
     * @param deniedPermissions 勾选不再提醒 为true，此时需要给出弹框提醒用户
     */
    private void processingResults(String deniedPermissions, boolean noMoreInquiries,
                                   OnGetPermissionListener permissionListener, String desc) {
        if (hasPermission(mContext, deniedPermissions)) {
            // TODO 执行拥有权限时的下一步。
            permissionListener.onGetPermissionYes();
        } else {

            // 第二种：用自定义的提示语。
            if (noMoreInquiries) {
                dialog(desc, permissionListener);
            } else {
                if (permissionListener != null)
                    permissionListener.onGetPermissionNo();
//                ToastUtil.showMessage(desc);
            }
        }
    }

    /**
     * 处理获取权限返回数据
     *
     * @param deniedPermissions
     * @param noMoreInquiries   勾选不再提醒 为true，此时需要给出弹框提醒用户
     */
    private void processingResults(String[] deniedPermissions, boolean noMoreInquiries,
                                   OnGetPermissionListener permissionListener, String desc) {
        if (hasPermissionGroup(mContext, deniedPermissions)) {
            // TODO 执行拥有权限时的下一步。
            if (permissionListener != null)
                permissionListener.onGetPermissionYes();
        } else {
            // 第二种：用自定义的提示语。
            if (noMoreInquiries) {
                dialog(desc, permissionListener);
            } else {
                if (permissionListener != null)
                    permissionListener.onGetPermissionNo();
            }
        }
    }

    /**
     * 判断单个权限是否拥有
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        String op = AppOpsManagerCompat.permissionToOp(permission);
        if (!TextUtils.isEmpty(op)) {
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 以权限组的形式判断是否拥有权限
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public boolean hasPermissionGroup(
            @NonNull Context context,
            @NonNull String[] deniedPermissions) {

        if (deniedPermissions.length == 0) {
            return false;
        }

        for (String permission : deniedPermissions) {
            boolean has = hasPermission(context, permission);
            if (!has) {
                return false;
            }
        }
        return true;
    }

    private String lastDesc;

    /**
     * 因为用户标记不在询问 获取权限 所以需要弹框提醒
     *
     * @param desc
     */
    public void dialog(String desc, OnGetPermissionListener permissionListener) {
        /*if (lastDesc != null && lastDesc.equals(desc)) {
            return;
        }
        if (StringUtil.isBlank(desc)) {
            return;
        }
        DayHrPublicDialog dialog = new DayHrPublicDialog(mContext, new DayHrPublicDialog.OnDayHrPublicDialogListener() {
            @Override
            public void executeTask() {
                goSetting();
                lastDesc = null;
                if (setPermissionListener != null)
                    setPermissionListener.onSetPermissionNo();
            }

            @Override
            public void cancelTask() {
                lastDesc = null;
                if (permissionListener != null)
                    permissionListener.onGetPermissionNo();
            }
        });
        dialog.setContent(desc);
        dialog.setTittleText("权限提醒");
        dialog.setSureButtonText("好，去设置");
        dialog.setCancelButtonText("取消");
        dialog.show();
        lastDesc = desc;*/
    }

    public void goSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    public static interface OnGetPermissionListener {
        void onGetPermissionYes();

        void onGetPermissionNo();
    }

    public static interface OnSetPermissionListener {
        //跳到设置中之后设置权限给一个回调
        void onSetPermissionNo();
    }
}
