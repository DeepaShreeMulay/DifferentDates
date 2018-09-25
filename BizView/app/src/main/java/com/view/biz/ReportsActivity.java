package com.view.biz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.view.biz.model.ItemDetails;

import java.util.ArrayList;

/**
 * Created by sharvari on 25-Aug-18.
 */

public class ReportsActivity extends AppCompatActivity {

    ListView listview;
    Context parent;
    ImageView img_calender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiloss_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview= (ListView) findViewById(R.id.listview);

        parent = ReportsActivity.this;
        img_calender= (ImageView) findViewById(R.id.img_calender);
        img_calender.setVisibility(View.VISIBLE);
        img_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ViewDialog vd = new ViewDialog();
                vd.showDialog(parent);
            }
        });


        final ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
        results.add(new ItemDetails("Expenses",R.drawable.icon_expenses));
        results.add(new ItemDetails("Top Report",R.drawable.icon_report));
        results.add(new ItemDetails("Inactive Customer",R.drawable.icon_customers));
        results.add(new ItemDetails("Inactive Items",R.drawable.icon_items));
        results.add(new ItemDetails("Ledger Report",R.drawable.icon_ledger));
        results.add(new ItemDetails("Day Book",R.drawable.icon_book));
//        results.add(new ItemDetails("Pending Sales Order",R.drawable.icon_sales));
//        results.add(new ItemDetails("Pending Purchase Order",R.drawable.icon_expenses));
        results.add(new ItemDetails("Profit Loss",R.drawable.icon_profit));
        listview.setAdapter(new ItemListBaseAdapter(this, results));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  value=results.get(position).getName();

                if (value.equalsIgnoreCase("Profit Loss")){
                    startActivity(new Intent(ReportsActivity.this,ProfitAndLossActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if (value.equalsIgnoreCase("Day Book")){
                    startActivity(new Intent(ReportsActivity.this,DayBookActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Inactive Items")) {
                    startActivity(new Intent(ReportsActivity.this, InactiveItemsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Ledger Report")) {
                    startActivity(new Intent(ReportsActivity.this, LedgerReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Expenses")) {
                    startActivity(new Intent(ReportsActivity.this, ExpensesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });
    }


    public class ItemListBaseAdapter extends BaseAdapter {
        private ArrayList<ItemDetails> itemDetailsrrayList;

        private LayoutInflater l_Inflater;

        public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
            itemDetailsrrayList = results;
            l_Inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);        }

        public int getCount() {
            return itemDetailsrrayList.size();
        }

        public Object getItem(int position) {
            return itemDetailsrrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = l_Inflater.inflate(R.layout.common_list_item_lay, null);
                holder = new ViewHolder();
                holder.txt_Name = (TextView) convertView.findViewById(R.id.txt_name);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.img_icon);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_Name.setText(itemDetailsrrayList.get(position).getName());
            holder.itemImage.setImageResource(itemDetailsrrayList.get(position).getImage());

            return convertView;
        }

        class ViewHolder {
            TextView txt_Name;
            ImageView itemImage;
        }
    }



}
