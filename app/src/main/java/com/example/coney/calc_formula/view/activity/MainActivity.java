package com.example.coney.calc_formula.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.R;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.view.KeyboardStatusDetector;
import com.example.coney.calc_formula.view.PaintBoard;
import com.example.coney.calc_formula.view.PaintData;

import java.io.IOException;

/**
 * @author coney
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int UPDATE_INPUT     = 0x00;
    public static final int ENABLE_REDO_BTN  = 0x01;
    public static final int DISABLE_REDO_BTN = 0x02;
    public static final int ENABLE_UNDO_BTN  = 0x03;
    public static final int DISABLE_UNDO_BTN = 0x04;

    private EditText input;
    private PaintBoard paintBoard;
    private ImageButton imgBtnSave;
    private ImageButton loadBtn;
    private ImageButton redoBtn;
    private ImageButton undoBtn;

    private Handler mHandler;
    private int keyboardHeight = 0;
    /**
     *   读写权限
     */
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 请求状态码
     */
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authorityRequest();
        init();
    }

    private void init(){
        imgBtnSave = findViewById(R.id.main_activity_ibtn_save);
        loadBtn= findViewById(R.id.main_activity_ibtn_open);
        redoBtn = findViewById(R.id.main_activity_ibtn_redo);
        undoBtn = findViewById(R.id.main_activity_ibtn_undo);
        input = findViewById(R.id.main_activity_et_input);
        paintBoard = findViewById(R.id.main_activity_paintboard);
        paintBoard.setInputText(input);

        redoBtn.setEnabled(false);
        undoBtn.setEnabled(false);
        redoBtn.setClickable(false);
        undoBtn.setClickable(false);

        imgBtnSave.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        redoBtn.setOnClickListener(this);
        undoBtn.setOnClickListener(this);
        new KeyboardStatusDetector()
                .registerActivity(this)
                .setVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
                    @Override
                    public void onVisibilityChanged(boolean keyboardVisible) {
                        PaintData paintData = paintBoard.getPaintData();
                        DataHelper helper = new DataHelper(paintData);
                        if (keyboardVisible){
                            if (keyboardHeight == 0){
                                Rect rect = new Rect();
                                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                                keyboardHeight = getWindowManager().getDefaultDisplay().getHeight() - rect.bottom;
                            }
                            int selcY = (int) helper.getYByRow(paintData.getSelectedRowId());
                            //行高
                            int gribHeigh = (int) helper.getRowHeightByRowId(paintData.getSelectedRowId());
                            //输入框与view之间最小的间隔
                            int inputGap = 80;
                            int thresholdY = input.getTop() - inputGap - gribHeigh;
                            if (selcY >= thresholdY){
                                paintData.setVerticalOffset(paintData.getVerticalOffset() + selcY - thresholdY);
                            }
                        }
                    }
                });

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case ENABLE_REDO_BTN:
                        redoBtn.setEnabled(true);
                        redoBtn.setClickable(true);
                        break;
                    case DISABLE_REDO_BTN:
                        redoBtn.setEnabled(false);
                        redoBtn.setClickable(false);
                        break;
                    case ENABLE_UNDO_BTN:
                        undoBtn.setEnabled(true);
                        undoBtn.setClickable(true);
                        break;
                    case DISABLE_UNDO_BTN:
                        undoBtn.setEnabled(false);
                        undoBtn.setClickable(false);
                        break;
                    case UPDATE_INPUT:
                        input.setText(msg.arg1);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        paintBoard.setMainHandler(mHandler);
    }

    /**
     * 请求权限
     */
    private void authorityRequest(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            PERMISSIONS_STORAGE,
                            REQUEST_PERMISSION_CODE);
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE){
            Log.i("MainActivity", "申请的权限为：" + permissions[0] + ",申请结果：" + grantResults[0]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_activity_ibtn_open:
                paintBoard.loadFile("sheet1.xml");
                Toast.makeText(this,"载入文件...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_activity_ibtn_save:
                FileOper fileOper = new FileOper();
                try {
                    fileOper.saveSheetToFile(paintBoard.getPaintData().getPresentSheet(),"sheet1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"已保存",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_activity_ibtn_redo:
                Toast.makeText(this,"点击还原",Toast.LENGTH_SHORT).show();
                paintBoard.redo();
                break;
            case R.id.main_activity_ibtn_undo:
                Toast.makeText(this,"点击撤销",Toast.LENGTH_SHORT).show();
                 paintBoard.undo();
                break;
            default:
                break;
        }
    }


    //
//    public void onSaveBtnClick(View v){
//        FileOper fileOper = new FileOper();
//        try {
//            fileOper.saveSheetToFile(paintBoard.getPaintData().getPresentSheet(),"sheet1");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Toast.makeText(this,"已保存",Toast.LENGTH_SHORT).show();
//    }
//    public void onOpenBtnClick(View v){
//      paintBoard.loadFile("sheet1.xml");
//      Toast.makeText(this,"载入文件...",Toast.LENGTH_SHORT).show();
//    }
}
