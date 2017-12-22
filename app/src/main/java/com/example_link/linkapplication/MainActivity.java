package com.example_link.linkapplication;

        import android.content.ContentValues;
        import android.content.DialogInterface;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.CursorAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.TextView;
        import android.widget.Toolbar;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;



public class MainActivity extends AppCompatActivity {



    private List<Map<String, String>> mStringList = new ArrayList<>();
    private ListView mListView;
    private Button mBtn_insert;
    private EditText mEt_peopleName;
    private EditText mEt_phone;
    private EditText mEt_delet_peopleName;
    private EditText mEt_delet_phone;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    private SQLiteDatabase mDbWriter;
    private SQLiteDatabase mDbReader;
    private MySQLite mMySQLite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        initView();
        initEvent();
        mBtn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                mEt_peopleName.setText("");    //数据添加完成后，清空文本
                mEt_phone.setText("");
            }
        });

        //长按删除item
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("您确定要删除他（她）吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData(position);
                            }
                        })
                        .setNegativeButton("放弃", null)
                        .show();
                return true;
            }
        });


        //单击修改item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                View mView = View.inflate(MainActivity.this, R.layout.delet_et_layout,null);       //将布局delet_et_layout渲染成view对象
                mEt_delet_peopleName = (EditText) mView.findViewById(R.id.et_delet_peoplename);       //用对应布局的view对象去findViewById获取控件对象
                mEt_delet_phone = (EditText) mView.findViewById(R.id.et_delet_phone);
                mEt_delet_peopleName.setText(((TextView) view.findViewById(R.id.peoplename)).getText());   //获取并显示原联系人姓名
                mEt_delet_phone.setText(((TextView) view.findViewById(R.id.phone)).getText());       //获取并显示原联系人手机号
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("请修改数据")
                        .setView(mView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.myListview);
        mBtn_insert = (Button) findViewById(R.id.btn_insert);

        mEt_peopleName = (EditText) findViewById(R.id.et_peoplename);
        mEt_phone = (EditText) findViewById(R.id.et_phone);
    }

    private void initEvent() {
        mMySQLite = new MySQLite(this);
        mDbWriter = mMySQLite.getWritableDatabase();
        mDbReader = mMySQLite.getReadableDatabase();

        mSimpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.listview_xs_item,null,
                new String[]{"songname", "singer"}, new int[]{R.id.peoplename, R.id.phone}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mListView.setAdapter(mSimpleCursorAdapter);     //给ListView设置适配器
        refreshListview();

    }

    //定义刷新数据列表的方法
    public void refreshListview() {
        Cursor mCursor = mDbWriter.query("phone_book", null, null, null, null, null, null);
        mSimpleCursorAdapter.changeCursor(mCursor);
    }

    //增加联系人
    public void insertData() {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("songname", mEt_peopleName.getText().toString().trim());
        mContentValues.put("singer", mEt_phone.getText().toString().trim());
        mDbWriter.insert("phone_book", null, mContentValues);
        refreshListview();
    }

    //删除联系人
    public void deleteData(int positon) {
        Cursor mCursor = mSimpleCursorAdapter.getCursor();
        mCursor.moveToPosition(positon);
        int itemId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        mDbWriter.delete("phone_book", "_id=?", new String[]{itemId + ""});
        refreshListview();
    }

    //修改联系人信息
    public void updateData(int positon) {
        Cursor mCursor = mSimpleCursorAdapter.getCursor();
        mCursor.moveToPosition(positon);
        int itemId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("songname", mEt_delet_peopleName.getText().toString().trim());
        mContentValues.put("singer", mEt_delet_phone.getText().toString().trim());
        mDbWriter.update("phone_book", mContentValues, "_id=?", new String[]{itemId + ""});
        refreshListview();
    }

}