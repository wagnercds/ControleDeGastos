package com.blogspot.tudoqueeinteressante.controlegastos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

public class CadastroDespesas extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	
	private ArrayList<CheckBox> listaCad = new ArrayList<CheckBox>();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastrodespesas);
		
		// Clique no botão para inserir nova despesa
        Button btnNovaDespesa = (Button)findViewById(R.id.btnNovaDespesa);
        btnNovaDespesa.setOnClickListener(onClickBtnNovaDespesa);
        
        // Botão para excluir despesas
        Button btnExcluir = (Button)findViewById(R.id.btnExcluirDespesas);
		btnExcluir.setOnClickListener(onClickBtnExcluirDespesas);
		
		// Botão para alterar despesas
		Button btnEditar = (Button)findViewById(R.id.btnEditarDespesas);
		btnEditar.setOnClickListener(onClickBtnEditarDespesas);
		
		populaTela();       
        
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		populaTela();
	}
	
	private void populaTela()
	{
		// Insere as despesas cadastradas
        LinearLayout srvDespesas = (LinearLayout)findViewById(R.id.srvDespesas);
        srvDespesas.removeAllViews();
        Cursor dados = getContentResolver().query(tblDespesas.TblCadDespesas.CONTENT_URI, null, null, null, null);
        dados.moveToFirst();        
        listaCad.clear();   
        
		while (! dados.isAfterLast()) 
		{
			CheckBox desp = new CheckBox(getBaseContext());
			desp.setTag(dados.getInt(dados.getColumnIndex(tblDespesas.TblCadDespesas.cd_ID)));
			desp.setText(dados.getString(dados.getColumnIndex(tblDespesas.TblCadDespesas.ds_Despesa)));
            srvDespesas.addView(desp);
            listaCad.add(desp);
            dados.moveToNext();
		}
		
		if (! dados.isClosed())
			dados.close();		
	}
	
	private OnClickListener onClickBtnNovaDespesa = new OnClickListener() {  
    	public void onClick(View v) {    		
    		Intent myIntent = new Intent(v.getContext(), InsAltDespesa.class);  
            startActivity(myIntent);  
    	}  
    };
    
    private OnClickListener onClickBtnExcluirDespesas = new OnClickListener() {  
    	public void onClick(View v) {
    		int totDel = 0;
    		
    		for (CheckBox desp : listaCad)
    		{
    			if (desp.isChecked())    			    				
    				totDel += getContentResolver().delete(tblDespesas.TblCadDespesas.CONTENT_URI, 
    													  tblDespesas.TblCadDespesas.cd_ID + " = " + desp.getTag().toString(),
    													  null);    			
    		}
    		if (totDel > 0)    		
    		{
    			populaTela();
    			Toast.makeText(CadastroDespesas.this, "Total de registros excluidos: " + totDel, Toast.LENGTH_SHORT).show();
    		}
    		else
    			Toast.makeText(CadastroDespesas.this, "Não foi selecionado nenhum registro !", Toast.LENGTH_SHORT).show();
    	}  
    };
    
    private OnClickListener onClickBtnEditarDespesas = new OnClickListener() {
    	public void onClick(View v) {    		
    		boolean aux = false;
    		
    		ArrayList<Integer> lstCodigo = new ArrayList<Integer>();    		    		
            
    		for (CheckBox desp : listaCad)
    		{    			
    			if (desp.isChecked())
    			{
    				lstCodigo.add((Integer) desp.getTag());    				    	    		    	    		
    	    		aux = true;    	    		
    			}
    		}
    		
    		if (aux)
    		{    			   		
    			Intent myIntent = new Intent(v.getContext(), InsAltDespesa.class);
    			myIntent.putIntegerArrayListExtra("codigos", lstCodigo);
    			startActivity(myIntent);    			
    		}
    	}
    };
}
