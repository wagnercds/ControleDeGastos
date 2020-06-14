package com.blogspot.tudoqueeinteressante.controlegastos;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManLancamento extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@SuppressWarnings("unused")
	private Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.altlancamento);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null)
		{		
			TextView txtTitLancamento = (TextView)findViewById(R.id.txtTitLancamento);    		
			txtTitLancamento.setText(R.string.titDelLancamentos);
			
			Button btnFunc = (Button)findViewById(R.id.btnEditarLancamento2);
			btnFunc.setText(R.string.btnExcluir);
		}
		else {
			Cursor dados = getContentResolver().query(
    				tblDespesas.vwLancamentoDespesas.CONTENT_URI, 
    				null, 
    				tblDespesas.vwLancamentoDespesas.join_Tables,
    				null, 
    				tblDespesas.vwLancamentoDespesas.dt_Lancamento + " DESC");    		    		
            dados.moveToFirst();
            
            LinearLayout srvLancamentos = (LinearLayout)findViewById(R.id.srvAltDelLancamentos);            
         
            while (! dados.isAfterLast()) 
    		{
            	LinearLayout linha = new LinearLayout(getBaseContext());
            	linha.setOrientation(0);
            	
            	CheckBox lanCheck = new CheckBox(getBaseContext());    			
    			lanCheck.setTag(dados.getInt(dados.getColumnIndex(tblDespesas.vwLancamentoDespesas.cd_IDLancamento)));
    			linha.addView(lanCheck);
    			
    			TextView txtDataLancamento = new TextView(getBaseContext());    			
    			txtDataLancamento.setText(dados.getString(dados.getColumnIndex(tblDespesas.vwLancamentoDespesas.dt_Lancamento)));
    			txtDataLancamento.setWidth(100);
    			linha.addView(txtDataLancamento);
    			
    			TextView txtNomeDespesas = new TextView(getBaseContext());    			
    			txtNomeDespesas.setText(dados.getString(dados.getColumnIndex(tblDespesas.vwLancamentoDespesas.ds_Despesa)));
    			txtNomeDespesas.setWidth(150);
    			linha.addView(txtNomeDespesas);
    			
    			TextView txtValorLancamento = new TextView(getBaseContext());    			
    			txtValorLancamento.setText(dados.getString(dados.getColumnIndex(tblDespesas.vwLancamentoDespesas.vl_Lancamento)));    			
    			linha.addView(txtValorLancamento);
    			
    			srvLancamentos.addView(linha);
                dados.moveToNext();    		
    		}
            
            if (! dados.isClosed())
            	dados.close();
		}
	}
}
