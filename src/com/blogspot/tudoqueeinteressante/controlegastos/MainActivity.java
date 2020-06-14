package com.blogspot.tudoqueeinteressante.controlegastos;

import com.blogspot.tudoqueeinteressante.controlegastos.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
			
	
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
               
        // Clique no cadastro de despesas        
        navBtnCadDespesas();
        // Clique no botão lançamento
        navBtnLancamentos();
    }
    
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if (telaRetorno != -1) {
        		setContentView(telaRetorno);        		
        		switch (telaRetorno) {
        			case R.layout.cadastrodespesas :
        				onClickBtnCadDespesas.onClick(null);        				
        				break;
        			case R.layout.lancamentos :        				
        				break;
        			default :        				
        				telaRetorno = -1;
        		}
        		return true;
        	}
        }
     
        return super.onKeyDown(keyCode, event);     
    }*/
    
    private OnClickListener onClickBtnCadDespesas = new OnClickListener() {  
    	public void onClick(View v) {
    		Intent myIntent = new Intent(v.getContext(), CadastroDespesas.class);  
            startActivity(myIntent);  
    	}   		
    		  
    };
    
    // Botões navegão principal
    private void navBtnCadDespesas() {
    	ImageButton btnCadDespesas = (ImageButton)findViewById(R.id.btnCadDespesas);
        btnCadDespesas.setOnClickListener(onClickBtnCadDespesas);
    }
    
    private void navBtnLancamentos() {
    	ImageButton btnLancamentos = (ImageButton)findViewById(R.id.btnLancamentos);
    	btnLancamentos.setOnClickListener(onClickBtnLancamentos);
    }    
    
    
    
    private OnClickListener onClickBtnLancamentos = new OnClickListener() {  
    	public void onClick(View v) {
    		Intent myIntent = new Intent(v.getContext(), CadastroLancamento.class);  
            startActivity(myIntent);    		
    	}    		
    };
    
    
    
    
    
    
}