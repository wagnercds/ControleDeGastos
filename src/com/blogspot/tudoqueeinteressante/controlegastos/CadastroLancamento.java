package com.blogspot.tudoqueeinteressante.controlegastos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CadastroLancamento extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lancamentos);
		
		// Clique no botão para inserir nova despesa
        Button btnNovoLancamento = (Button)findViewById(R.id.btnNovoLancamento);
        btnNovoLancamento.setOnClickListener(onClickBtnNovoLancamento);
        
        // Clique para alterar lançamentos
        Button btnEditarLancamento = (Button)findViewById(R.id.btnEditarLancamento);
        btnEditarLancamento.setOnClickListener(onClickBtnEditarLancamento);
        
        // Clique para excluir lancamentos
        Button btnExcluirLancamento = (Button)findViewById(R.id.btnExcluirLancamento);
        btnExcluirLancamento.setOnClickListener(onClickBtnExcluirLancamento);
	}
	
	private OnClickListener onClickBtnNovoLancamento = new OnClickListener() {  
    	public void onClick(View v) {    		
    		Intent myIntent = new Intent(v.getContext(), InsAltLancamento.class);  
            startActivity(myIntent);  
    	}    		
    };
    
    private OnClickListener onClickBtnEditarLancamento = new OnClickListener() {  
    	public void onClick(View v) {
    		Intent myIntent = new Intent(v.getContext(), ManLancamento.class);    		
            startActivity(myIntent);     
    	}
    };
    
    private OnClickListener onClickBtnExcluirLancamento = new OnClickListener() {  
    	public void onClick(View v) {    		
    		Intent myIntent = new Intent(v.getContext(), ManLancamento.class);
    		myIntent.putExtra("exclusao", true);
            startActivity(myIntent); 
    	}    		
    };
}
