package com.blogspot.tudoqueeinteressante.controlegastos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InsAltLancamento extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	
	private int listaSelect[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.inserelancamento);
		
		// Carrega o Spinner com as despesas cadastradas
		Cursor dados = getContentResolver().query(tblDespesas.TblCadDespesas.CONTENT_URI, null, null, null, null);
		String cadDespesas[] = new String[dados.getCount()];    		
        dados.moveToFirst();
        listaSelect = new int[dados.getCount()];
        int loop = 0;
        while (! dados.isAfterLast()) 
		{
        	listaSelect[loop] = dados.getInt(dados.getColumnIndex(tblDespesas.TblCadDespesas.cd_ID));
        	cadDespesas[loop++] = dados.getString(dados.getColumnIndex(tblDespesas.TblCadDespesas.ds_Despesa));
            dados.moveToNext();
		}
        
        Spinner cbDespesas = (Spinner)findViewById(R.id.cbDespesasLancamento);        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, cadDespesas);
        cbDespesas.setAdapter(adapter);
		
		Button btnSalvarLancamento = (Button)findViewById(R.id.btnSalvarLancamento);
		btnSalvarLancamento.setOnClickListener(onClickBtnSalvarLancamento);
	}
	
	private OnClickListener onClickBtnSalvarLancamento = new OnClickListener() {  
    	public void onClick(View v) {
    		EditText txtValorLancamento = (EditText)findViewById(R.id.txtValorLancamento);
    		if (txtValorLancamento.getText().toString().trim().length() > 0 )
    		{
    			ContentValues values = new ContentValues();
    			
    			DatePicker dtLancamento = (DatePicker)findViewById(R.id.dtpLancamento);
    			values.put(tblDespesas.TblCadLancamento.dt_Lancamento, dtLancamento.getYear() + "-" + dtLancamento.getMonth() + "-" + dtLancamento.getDayOfMonth());        		
    			
    			Spinner cbDespesas = (Spinner)findViewById(R.id.cbDespesasLancamento);    			
        		values.put(tblDespesas.TblCadLancamento.cd_Despesa, listaSelect[cbDespesas.getSelectedItemPosition()]);
        		
        		values.put(tblDespesas.TblCadLancamento.vl_Lancamento, txtValorLancamento.getText().toString());
        		
        		try
        		{
        			getContentResolver().insert(tblDespesas.TblCadLancamento.CONTENT_URI, values);
        		}
        		catch (Exception e)
        		{
        			Toast.makeText(InsAltLancamento.this, e.getMessage(), Toast.LENGTH_LONG).show();
        		}
        		
        		txtValorLancamento.setText("");        		        		
        		dtLancamento.requestFocus();
        		Toast.makeText(InsAltLancamento.this, "Lançamento salvo com sucesso !", Toast.LENGTH_SHORT).show();
    		}
    		else {    			
    			Toast.makeText(InsAltLancamento.this, "É obrigatório o preenchimento do valor da despesa !", Toast.LENGTH_SHORT).show();
    			txtValorLancamento.requestFocus();
    		}
    	}    		
    };
}
