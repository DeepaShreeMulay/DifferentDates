package com.view.biz.adapter;

/**
 * Created by Admin on 3/20/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.view.biz.R;

import java.util.ArrayList;
import java.util.HashMap;

//Adapter class extends with BaseAdapter and implements with OnClickListener
public class QuestionsAdapter extends BaseAdapter {

    private Context slcntx;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private static LayoutInflater inflater=null;
    String usertype;
    SharedPreferences sharedPreferences;

    public QuestionsAdapter() {
        this.data = new ArrayList<HashMap<String, String>>();
    }


    public QuestionsAdapter(Context ctx, ArrayList<HashMap<String, String>> d) {
        slcntx = ctx;
        data=d;

        inflater = (LayoutInflater)slcntx.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView txt_title,txt_amount;
        ImageView img_tick;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;
        HashMap<String, String> balencelist = new HashMap<String, String>();

        balencelist = data.get(position);

        if(convertView==null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.balence_list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();

            holder.txt_title = (TextView) vi.findViewById(R.id.txt_title);
            holder.txt_amount = (TextView) vi.findViewById(R.id.txt_amount);

            vi.setTag(holder);
        } else {

            holder = (ViewHolder) vi.getTag();

        }

        holder.txt_title.setText(balencelist.get("DSPDISPNAME"));
        holder.txt_title.setAllCaps(false);
        String mainamount=balencelist.get("BSMAINAMT");
        //holder.txt_amount.setText("\u20B9" + mainamount);
        holder.txt_amount.setText(mainamount);
        if (mainamount==null){

        }else {
            if (mainamount.equals("")) {
                String subamount = balencelist.get("PLSUBAMT");
               // holder.txt_amount.setText("\u20B9" + subamount);
                holder.txt_amount.setText(mainamount);

            } else {
               // holder.txt_amount.setText("\u20B9" + mainamount);
                holder.txt_amount.setText(mainamount);

            }
        }


        return vi;
    }





}



