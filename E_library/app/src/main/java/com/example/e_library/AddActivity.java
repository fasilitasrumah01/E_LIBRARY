package com.example.e_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_library.adapter.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kotlin.Suppress;

public class AddActivity extends AppCompatActivity {
    DBHelper dbHelper;
    TextView Tvstatus;
    Button Btnproses;
    EditText TxID, TXNama, TxJudul, TxtglPinjam, TxtglKembali, TxStatus;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbHelper = new DBHelper(this);
        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxID = (EditText) findViewById(R.id.txID);
        TxJudul = (EditText) findViewById(R.id.txJudulBuku);
        TxtglPinjam = (EditText) findViewById(R.id.txPinjam);
        TxtglKembali = (EditText) findViewById(R.id.txKembali);
        TxStatus = (EditText) findViewById(R.id.txStatus);

        Tvstatus = (TextView) findViewById(R.id.tVStatus);
        Btnproses = (Button) findViewById(R.id.btnproses);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        getData();

        TxtglKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        Btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proseskembali();
            }
        });
        ActionBar menu = getSupportActionBar();
        menu.setDisplayHomeAsUpEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);


    }

    private void proseskembali() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setMessage("Proses ke Pengembalian buku ? ");
        builder.setCancelable(true);
        builder.setPositiveButton("proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String idpinjam = TxID.getText().toString().trim();
                String kembali = "Dikembalikan";

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_status, kembali);
                dbHelper.updateData(values, id);
                Toast.makeText(AddActivity.this,"Proses pengembalian berhasil",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMont) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMont);
                TxtglKembali.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdfl = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
        String txtglPinjam = sdfl.format(cl.getTime());
        TxtglPinjam.setText(txtglPinjam);

        Cursor cur = dbHelper.oneData(id);
        if (cur.moveToFirst()){
            @SuppressLint("Range") String idpinjam = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            @SuppressLint("Range") String nama = cur.getString(cur.getColumnIndex(DBHelper.row_nama));
            @SuppressLint("Range") String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            @SuppressLint("Range") String pinjam = cur.getString(cur.getColumnIndex(DBHelper.row_pinjam));
            @SuppressLint("Range") String kembali = cur.getString(cur.getColumnIndex(DBHelper.row_kembali));
            @SuppressLint("Range") String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            TxID.setText(idpinjam);
            TXNama.setText(nama);
            TxJudul.setText(judul);
            TxtglPinjam.setText(pinjam);
            TxtglKembali.setText(kembali);
            TxStatus.setText(status);

            if (TxJudul.equals("")){
                Tvstatus.setVisibility(View.GONE);
                TxStatus.setVisibility(View.GONE);
                Btnproses.setVisibility(View.GONE);
            }else{
                Tvstatus.setVisibility(View.VISIBLE);
                TxStatus.setVisibility(View.VISIBLE);
                Btnproses.setVisibility(View.VISIBLE);
            }
            if (status.equals("DIPINJAM")){
                Btnproses.setVisibility(View.GONE);
            }else{
                Btnproses.setVisibility(View.GONE);
                TXNama.setEnabled(false);
                TxJudul.setEnabled(false);
                TxtglKembali.setEnabled(false);
                TxStatus.setEnabled(false);

            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        String idpinjam = TxID.getText().toString().trim();
        String status = TxStatus.getText().toString().trim();

        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);

        if (idpinjam.equals("")){
            itemDelete.setVisible(false);
            itemClear.setVisible(true);
        }else{
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }
        if(status.equals("Dikemalikan")){
            itemDelete.setVisible(false);
            itemClear.setVisible(false);
            itemSave.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertAndUpdate();
        }switch (item.getItemId()){
            case R.id.action_clear:
                TXNama.setText("");
                TxJudul.setText("");
                TxtglKembali.setText("");
        }switch (item.getItemId()){
            case R.id.action_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage("Data ini akan di hapus");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dbHelper.deleteData(id);
                        Toast.makeText(AddActivity.this,"terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertAndUpdate() {
        String idpinjam = TxID.getText().toString().trim();
        String nama = TXNama.getText().toString().trim();
        String judul = TxJudul.getText().toString().trim();
        String tglpinjam = TxtglPinjam.getText().toString().trim();
        String tglkembali = TxtglKembali.getText().toString().trim();
        String status = "Dipinjam";

        ContentValues values = new ContentValues();
        values.put(DBHelper.row_nama, nama);
        values.put(DBHelper.row_judul, judul);
        values.put(DBHelper.row_kembali, tglkembali);
        values.put(DBHelper.row_status, status);

        if (nama.equals("")||judul.equals("")||tglkembali.equals("")){
            Toast.makeText(AddActivity.this,"silahkan mengisi data dengan lengkap", Toast.LENGTH_SHORT).show();
        }else{
            if(idpinjam.equals("")){
                values.put(DBHelper.row_pinjam,tglpinjam);
                dbHelper.insertData(values);
            }else{
                dbHelper.updateData(values,id);
            }
            Toast.makeText(AddActivity.this,"data tersimpan", Toast.LENGTH_SHORT).show();
        }

    }
}