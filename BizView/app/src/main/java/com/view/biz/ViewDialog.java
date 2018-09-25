package com.view.biz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewDialog {
    String[] dates;
    String StartDate="dummydate", EndDate="dummydate";
    SimpleDateFormat fdf;
    Context parent;

    public void showDialog(Context activity){
        parent = activity;
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialoglistview);
        myDialog.setCancelable(true);

        TextView quest = (TextView) myDialog.findViewById(R.id.dialoginfogototextsmall1);
        ListView cliplist = (ListView) myDialog.findViewById(R.id.dateselectlist);
        quest.setText("Select Date");
        dates =  parent.getResources().getStringArray(R.array.date_array);
        fdf = new SimpleDateFormat("dd-MMM-yyyy");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
                R.layout.select_dialog_list_item, dates);

        cliplist.setAdapter(adapter1);

        cliplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                createStartEndDate(dates[position]);
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    public void createStartEndDate(String datetype) {
        switch (datetype){
            case "Today" :
                Date c = Calendar.getInstance().getTime();
                StartDate = fdf.format(c);
                EndDate = fdf.format(c);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "Yesterday" :
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                Date c1 = cal.getTime();
                StartDate = fdf.format(c1);
                EndDate = fdf.format(c1);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "This Week" :
                Calendar cal1 = Calendar.getInstance();
                cal1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                Date c2 = cal1.getTime();
                StartDate = fdf.format(c2);
                cal1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                c2 = cal1.getTime();
                EndDate = fdf.format(c2);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "This Month" :
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.DAY_OF_MONTH,cal2.getActualMinimum(Calendar.DAY_OF_MONTH)); //------>
                Date c3 = cal2.getTime();
                StartDate = fdf.format(c3);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                c3 = cal2.getTime();
                EndDate = fdf.format(c3);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "This Quarter" :
                Calendar cal5 = Calendar.getInstance();
                cal5.set(Calendar.MONTH, cal5.get(Calendar.MONTH)/3 * 3);
                cal5.set(Calendar.DAY_OF_MONTH,cal5.getActualMinimum(Calendar.DAY_OF_MONTH)); //------>
                Date c6 = cal5.getTime();
                StartDate = fdf.format(c6);
                cal5.set(Calendar.MONTH, cal5.get(Calendar.MONTH)/3 * 3 + 2);
                cal5.set(Calendar.DAY_OF_MONTH, cal5.getActualMaximum(Calendar.DAY_OF_MONTH));
                c6 = cal5.getTime();
                EndDate = fdf.format(c6);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "This Year" :
                Calendar cal3 = Calendar.getInstance();
                cal3.set(Calendar.DAY_OF_YEAR,cal3.getActualMinimum(Calendar.DAY_OF_YEAR)); //------>
                Date c4 = cal3.getTime();
                StartDate = fdf.format(c4);
                cal3.set(Calendar.DAY_OF_YEAR, cal3.getActualMaximum(Calendar.DAY_OF_YEAR));
                c4 = cal3.getTime();
                EndDate = fdf.format(c4);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "Last Year" :
                Calendar cal4 = Calendar.getInstance();
                cal4.add(Calendar.YEAR, -1);
                cal4.set(Calendar.DAY_OF_YEAR,cal4.getActualMinimum(Calendar.DAY_OF_YEAR)); //------>
                Date c5 = cal4.getTime();
                StartDate = fdf.format(c5);
                cal4.set(Calendar.DAY_OF_YEAR, cal4.getActualMaximum(Calendar.DAY_OF_YEAR));
                c5 = cal4.getTime();
                EndDate = fdf.format(c5);
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                break;
            case "Custom Date" :
                getDateRange();
                break;
        }


    }

    public void getDateRange() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.calview);
        myDialog.setCancelable(true);

        dates =  parent.getResources().getStringArray(R.array.date_array);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Calendar startYear = Calendar.getInstance();
        startYear.add(Calendar.YEAR, -1);
        startYear.set(Calendar.DAY_OF_YEAR,startYear.getActualMinimum(Calendar.DAY_OF_YEAR));

        final CalendarPickerView calendar = (CalendarPickerView) myDialog.findViewById(R.id.calendar_view);
        final Button okbtn = (Button) myDialog.findViewById(R.id.okbtn);
        Date today = new Date();

        calendar.init(startYear.getTime(), nextYear.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendar.highlightDates(getHolidays(today));


        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                return true;
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                ArrayList<Date> selectedDates = (ArrayList<Date>)calendar
                        .getSelectedDates();
                StartDate = fdf.format(selectedDates.get(0));
                EndDate = fdf.format(selectedDates.get(selectedDates.size()-1));
                Toast.makeText(parent, StartDate+" - "+EndDate,Toast.LENGTH_LONG).show();
                myDialog.dismiss();
            }
        });


        myDialog.show();
    }

    public ArrayList<Date> getHolidays(Date today){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "13-11-2018";
        dateInString = sdf.format(today);
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Date> holidays = new ArrayList<>();
        holidays.add(date);
        return holidays;
    }
}