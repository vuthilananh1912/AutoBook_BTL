package com.vtlallklmc.autobook_btl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vtlallklmc.autobook_btl.Main_Fragments.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    Button btnDate,btnTime, btnSave, btnCancel;
    String getDate, getTime;
    TextView tvDate, tvTime;
    long rawDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //ánh xạ
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        //nhận name của intent Detail Activity
        Intent receiveIntent = getIntent();
        String nameCar = receiveIntent.getStringExtra("name");

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDate==null || getTime == null){
                    AlertDialog.Builder require = new AlertDialog.Builder(BookingActivity.this);
                    require.setMessage("Vui lòng chọn ngày giờ đặt mua!");
                    require.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing
                        }
                    });
                    require.show();
                }else{
                    AlertDialog.Builder confirm = new AlertDialog.Builder(BookingActivity.this);
                    confirm.setTitle("Xác nhận đặt lịch");
                    confirm.setIcon(R.drawable.round_loyalty_24);
                    String message = "Xe ô tô "+nameCar+"\nNgày: "+getDate+"\nThời gian: "+getTime+"\nĐịa điểm: Showroom FITHOU - 96 Định Công, Thanh Xuân, Hà Nội.";
                    confirm.setMessage("Xác nhận đặt lịch mua xe:\n"+message);
                    confirm.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent addEventIntent = new Intent(Intent.ACTION_INSERT);

                            addEventIntent.setData(CalendarContract.CONTENT_URI);

                            addEventIntent.putExtra(CalendarContract.Events.TITLE,"Sự kiện mua xe "+nameCar.toString());
                            addEventIntent.putExtra(CalendarContract.Events.DESCRIPTION, message.toString());
                            addEventIntent.putExtra(CalendarContract.Events.EVENT_LOCATION,"96 Định Công, Thanh Xuân, Hà Nội");
//                            addEventIntent.putExtra(CalendarContract.Events.DTSTART, rawDate);
//                            addEventIntent.putExtra(CalendarContract.Events.DTEND, rawDate+60*60*1000);
//                            addEventIntent.putExtra(CalendarContract.Events.ALL_DAY,true);
//                            addEventIntent.putExtra(Intent.EXTRA_EMAIL,"20a10010156@students.hou.edu.vn");

                            startActivity(addEventIntent);
//                            if(addEventIntent.resolveActivity(getPackageManager())!=null){
//                                startActivity(addEventIntent);
//                            }else{
//                                Toast.makeText(BookingActivity.this, "Không thể tạo sự kiện trong ứng dụng lịch của bạn", Toast.LENGTH_SHORT).show();
//                            }
//                            ContentResolver cr = getContentResolver();
//                            ContentValues cv = new ContentValues();
//                            cv.put(CalendarContract.Events.TITLE,"Sự kiện mua xe "+nameCar);
//                            cv.put(CalendarContract.Events.DESCRIPTION, message);
//                            cv.put(CalendarContract.Events.EVENT_LOCATION,"96 Định Công, Thanh Xuân, Hà Nội");
////                            cv.put(CalendarContract.Events.DTSTART, rawDate);
////                            cv.put(CalendarContract.Events.DTEND, rawDate+60*60*1000);
////                            cv.put(CalendarContract.Events.CALENDAR_ID, "1");
////                            cv.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(Calendar.getInstance().getTimeZone()));
//
//                            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI,cv);
                        }
                    });
                    confirm.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent goHomeIntent = new Intent(BookingActivity.this, MainActivity.class);
                            startActivity(goHomeIntent);
                        }
                    });
                    confirm.show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHomeIntent = new Intent(BookingActivity.this, MainActivity.class);
                startActivity(goHomeIntent);
            }
        });
    }
    public void datePicker(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int nam, int thang, int ngay) {
                calendar.set(nam,thang,ngay);
                rawDate = calendar.getTimeInMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                getDate = simpleDateFormat.format(calendar.getTime());
                tvDate.setText("Ngày mua: "+simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }
    public void timePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int gio, int phut) {
                calendar.set(0,0,0,gio,phut);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                getTime = simpleDateFormat.format(calendar.getTime());
                tvTime.setText("Giờ mua: "+simpleDateFormat.format(calendar.getTime()));
            }
        },hour,minute,true);
        timePickerDialog.show();
    }
}