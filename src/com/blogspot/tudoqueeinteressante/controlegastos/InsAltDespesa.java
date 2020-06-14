package com.blogspot.tudoqueeinteressante.controlegastos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InsAltDespesa extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private Bundle extras;	
	private ArrayList<Integer> codigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.inseredespesas);		
		
		extras = getIntent().getExtras();
		Button btnSalvar = (Button)findViewById(R.id.btnSalvarDespesa);
		
		if (extras != null)
		{
			codigos = extras.getIntegerArrayList("codigos");
			getRegistro(codigos.get(0));			
			
			// Clique do botão para salvar a alteração
			btnSalvar.setOnClickListener(onClickBtnSalvarAlteracaoDespesas);
		}
		else		
			// Clique do botão salvar da inserção de despesa
			btnSalvar.setOnClickListener(onClickBtnSalvarDespesa);
	}
	
	private void getRegistro(int codigo)
	{		
		Cursor dados = getContentResolver().query(tblDespesas.TblCadDespesas.CONTENT_URI, 
												  new String[]{tblDespesas.TblCadDespesas.ds_Despesa}, 
												  tblDespesas.TblCadDespesas.cd_ID + "=" + codigo,
												  null,
												  null);
		if (dados.moveToFirst()) {
			TextView txtNomeDespesa = (TextView)findViewById(R.id.txtNomeDespesa);
			txtNomeDespesa.setText(dados.getString(dados.getColumnIndex(tblDespesas.TblCadDespesas.ds_Despesa)));
		}
		
		if (! dados.isClosed())
			dados.close();	
	}
	
	private OnClickListener onClickBtnSalvarDespesa = new OnClickListener() {  
    	public void onClick(View v) {
    		TextView txtNomeDespesa = (TextView)findViewById(R.id.txtNomeDespesa);
    		if (txtNomeDespesa.getText().toString().trim().length() > 0) {
    			ContentValues values = new ContentValues();
    			values.put(tblDespesas.TblCadDespesas.fl_Tipo, 0);        		
        		values.put(tblDespesas.TblCadDespesas.ds_Despesa, txtNomeDespesa.getText().toString());
        		getContentResolver().insert(tblDespesas.TblCadDespesas.CONTENT_URI, values);
        		Toast.makeText(InsAltDespesa.this, "Registro salvo com sucesso !", Toast.LENGTH_SHORT).show();
        		txtNomeDespesa.setText("");
    		}
    		else    			
    			Toast.makeText(InsAltDespesa.this, "É obrigatório o preenchimento do nome da despesa !", Toast.LENGTH_LONG).show();  			
    		   
    		txtNomeDespesa.requestFocus();
    	}  
    };
    
    private OnClickListener onClickBtnSalvarAlteracaoDespesas = new OnClickListener() {
    	public void onClick(View v) {
    		TextView txtNomeDespesa = (TextView)findViewById(R.id.txtNomeDespesa);    		
    		if (txtNomeDespesa.getText().toString().trim().length() > 0) { 
    			ContentValues values = new ContentValues();
    			values.put(tblDespesas.TblCadDespesas.ds_Despesa, txtNomeDespesa.getText().toString());
    			getContentResolver().update(tblDespesas.TblCadDespesas.CONTENT_URI, 
    										values, 
    										tblDespesas.TblCadDespesas.cd_ID + " = " + codigos.get(0),
    										null);    			//
    			Toast.makeText(InsAltDespesa.this, "Registro salvo com sucesso !", Toast.LENGTH_SHORT).show();
    			codigos.remove(0);
    			if (codigos.isEmpty()) {
    				finish();
    				return;
    			}
    			else 
    				getRegistro(codigos.get(0));
    		}
    		else {    			
    			Toast.makeText(InsAltDespesa.this, "É obrigatório o preenchimento do nome da despesa !", Toast.LENGTH_LONG).show();
    			
    		}
    		txtNomeDespesa.requestFocus();
    	}
    };
	
	
}
