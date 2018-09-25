package com.view.biz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.view.biz.model.ItemDetails;

import java.util.ArrayList;

/**
 * Created by sharvari on 25-Aug-18.
 */

public class DashboardActivity extends AppCompatActivity {

    ListView listview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiloss_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview= (ListView) findViewById(R.id.listview);

        final ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

        results.add(new ItemDetails("Sales-Credit Note(Gross)",R.drawable.icon_expenses));
        results.add(new ItemDetails("Purchase-Debit Note",R.drawable.icon_report));
        results.add(new ItemDetails("Receipt",R.drawable.icon_customers));
        results.add(new ItemDetails("Payment",R.drawable.icon_items));
        results.add(new ItemDetails("Receivable",R.drawable.icon_ledger));
        results.add(new ItemDetails("Payable",R.drawable.icon_book));
        results.add(new ItemDetails("Cash/Bank Balence",R.drawable.icon_sales));
        results.add(new ItemDetails("Sales Order",R.drawable.icon_expenses));
        results.add(new ItemDetails("Purchase Order",R.drawable.icon_profit));
        results.add(new ItemDetails("Delivery Note",R.drawable.icon_profit));
        results.add(new ItemDetails("Receipt Note",R.drawable.icon_profit));
        listview.setAdapter(new ItemListBaseAdapter(this, results));


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  value=results.get(position).getName();

                if (value.equalsIgnoreCase("Cash/Bank Balence")){
                    startActivity(new Intent(DashboardActivity.this,CashBalenceActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if (value.equalsIgnoreCase("Receivable")){
                    startActivity(new Intent(DashboardActivity.this,ReceivableActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if (value.equalsIgnoreCase("Payable")){
                    startActivity(new Intent(DashboardActivity.this,PayableActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if (value.equalsIgnoreCase("Sales-Credit Note(Gross)")){
                    startActivity(new Intent(DashboardActivity.this,SalesCreditNoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Purchase-Debit Note")) {
                    startActivity(new Intent(DashboardActivity.this,PurchaseDebitNoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Sales Order")) {
                    startActivity(new Intent(DashboardActivity.this,SalesRegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if(value.equals("Receipt")) {
                    startActivity(new Intent(DashboardActivity.this, ReceiptActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
