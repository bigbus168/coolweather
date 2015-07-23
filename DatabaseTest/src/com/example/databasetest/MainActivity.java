package com.example.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
        Button createDatabase = (Button)findViewById(R.id.create_database);
        Button addData = (Button)findViewById(R.id.add_data);
        Button updateData = (Button)findViewById(R.id.update_data);
        Button deleteData = (Button)findViewById(R.id.delete_data);
        Button querryData = (Button)findViewById(R.id.querry_data);
        Button replaceData = (Button)findViewById(R.id.replace_data);
        //Button versionNo = (Button)findViewById(R.id.version_no);
        
        replaceData.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		db.beginTransaction();   //开启事务
        		try{
        			db.delete("Book", null, null);
        			/*if(true){
        				//在这里手动抛出一个异常，让事务失败
        				throw new NullPointerException();
        			}*/
        			db.execSQL("insert into Book(name,author,pages,price)values(?,?,?,?)",
        					new String[]{"Game of Thrones","George Martin","720","20.85"});
        			db.setTransactionSuccessful();  //事务已经执行成功
        		}catch(Exception e){
        			e.printStackTrace();
        		}finally{
        			db.endTransaction();  //结束事务
        		}
        	}
        });
        querryData.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		//查询Book表中所有的数据
        		//Cursor cursor = db.query("Book", null, null, null, null, null, null);
        		Cursor cursor = db.rawQuery("select * from Book", null);
        		if(cursor.moveToFirst()){
        			do{
        				//遍历Cursor对象，取出数据并打印
        				String name = cursor.getString(cursor.getColumnIndex("name"));
        				String author = cursor.getString(cursor.getColumnIndex("author"));
        				int pages = cursor.getInt(cursor.getColumnIndex("pages"));
        				double price = cursor.getDouble(cursor.getColumnIndex("price"));
        				Log.d("MainActivity","book name is " + name);
        				Log.d("MainActivity","book author is "+ author);
        				Log.d("MainActivity","book pages is " + pages);
        				Log.d("MainActivity","book price is " + price);
        			}while(cursor.moveToNext());
        		}
        		cursor.close();
        	}
        });
        deleteData.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		//db.delete("Book", "pages > ?", new String[]{"500"});
        		db.execSQL("delete from Book where pages > ?",new String[]{"500"});
        	}
        });
        updateData.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		/*ContentValues values = new ContentValues();
        		values.put("price", 10.99);
        		db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});*/
        		db.execSQL("update Book set price = ? where name = ?",
        				new String[]{"10.11","The Da Vinci Code"});
        	}
        });
        addData.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		/*ContentValues values = new ContentValues();
        		//开始组装第一条数据
        		values.put("name", "The Da Vinci Code");
        		values.put("author", "Dan Brown");
        		values.put("pages", 454);
        		values.put("price", 16.96);
        		db.insert("Book", null, values);   //插入第一条数据
        		//开始组装第二条数据
        		values.put("name", "The Lost Symbol");
        		values.put("author","Dan Brown");
        		values.put("pages", 510);
        		values.put("price", 19.95);
        		db.insert("Book", null, values);  //插入第二条数据*/
        		db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
        				new String[]{"The Lost Symbol","Dan Brown","510","19.95"});
        		
        	}
        });
        createDatabase.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		dbHelper.getWritableDatabase();
        	}
        });
    }

    
}
