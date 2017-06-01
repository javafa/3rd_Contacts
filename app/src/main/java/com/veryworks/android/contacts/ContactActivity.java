package com.veryworks.android.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.veryworks.android.contacts.domain.Data;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        for(Data data : getContacts()){
            Log.i("Contacts","이름="+data.getName() +", tel="+data.getTel());
        }
    }

    public List<Data> getContacts() {
        // 데이터베이스 혹은 content resolver를 통해 가져온 데이터를 적재할
        // 데이터 저장소를 먼저 정의한다.
        List<Data> datas = new ArrayList<>();

        // 일종의 Database 관리툴
        // 전화번호부에 이미 만들어져 있는 Content Provider 를 통해서
        // 데이터를 가져올 수 있다.
        ContentResolver resolver = getContentResolver();

        // 1. 데이터 컨텐츠 URI (자원의 주소) 를 정의
        // 전화번호 URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // 2. 데이터에서 가져올 컬럼명을 정의
        String projections[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                ,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,ContactsContract.CommonDataKinds.Phone.NUMBER};
        // 3. Content Resolver로 쿼리를 날려서 데이터를 가져온다.
        Cursor cursor = resolver.query(phoneUri, projections, null, null, null);
        // 4. 반복문을 통해 cursor에 담겨있는 데이터를 하나씩 추출한다.
        if(cursor != null) {
            while(cursor.moveToNext()){
                // 4.1 위에정의한 프로젝션의 컬럼명으로 cursor 있는 인덱스값을 조회하고
                int idIndex = cursor.getColumnIndex(projections[0]);
                // 4.2 해당 index를 사용해서 실제값을 가져온다.
                int id = cursor.getInt(idIndex);

                int nameIndex = cursor.getColumnIndex(projections[1]);
                String name = cursor.getString(nameIndex);

                int telIndex = cursor.getColumnIndex(projections[2]);
                String tel = cursor.getString(telIndex);

                // 5. 내가 설계한 데이터 클래스에 담아준다.
                Data data = new Data();
                data.setId(id);
                data.setName(name);
                data.setTel(tel);

                // 6. 여러개의 객체를 담을 수 있는 저장소에 적재한다.
                datas.add(data);
            }
        }

        return datas;
    }
}
