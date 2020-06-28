package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {

     private FirebaseDatabase mFirebaseDatabase;
     private DatabaseReference mDatabaseReference;
     EditText txtTitle;
     EditText txtDescription;
     EditText txtPrice;
     TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_acivity);
        FirebaseUtil .openFbReference("traveldeals",this);
        mFirebaseDatabase=FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference=FirebaseUtil.mDatabaseReference;
        txtTitle=findViewById(R.id.txtTitle);
        txtDescription=findViewById(R.id.txtDescription);
        txtPrice=findViewById(R.id.txtPrice);
        Intent intent=getIntent();
        TravelDeal deal= (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal==null){
            deal=new TravelDeal();
        }
        this.deal=deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_SHORT).show();
                clean();
                backToList();
                return  true;
            case R.id.delete_deal:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted Successfully", Toast.LENGTH_SHORT).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clean() {
        txtPrice.setText("");
        txtTitle.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();

    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId()==null){
            mDatabaseReference.push().setValue(deal);
        }
        else {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }

    }
    private void deleteDeal(){
        if (deal==null){
            Toast.makeText(this, "Please Save the deal before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mDatabaseReference.child(deal.getId()).removeValue();
        }
    }
    private void backToList(){
        Intent intent=new Intent(this,ListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;

    }
}