
package caixa;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SupportCaixa {
    private javax.swing.JLabel labTotal;
    private javax.swing.JList list;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtQuant;
    private javax.swing.JTextField txtValor;
    private DefaultListModel<String> mod = new DefaultListModel<>();
    private ArrayList<Double> unitVal = new ArrayList<>();
    private double valTotal;
    DecimalFormat format = new DecimalFormat("#########0.00");

    public SupportCaixa(JLabel labTotal, JList list, JTextField txtCod, JTextField txtNome, JTextField txtQuant, JTextField txtValor) {
        this.labTotal = labTotal;
        this.list = list;
        this.txtCod = txtCod;
        this.txtNome = txtNome;
        this.txtQuant = txtQuant;
        this.txtValor = txtValor;
        
        txtCod.requestFocus();
        txtQuant.setText("1");
        list.setModel(mod);
    }
    
    public void listKey(KeyEvent evt){
        if(evt.getKeyCode()==127 && list.getSelectedIndex()!=-1){
            int selected = list.getSelectedIndex();
            mod.remove(selected);
            unitVal.remove(selected);
            setValTotal();
        }
    }
    
    public void txtCodKey(KeyEvent evt){
        
        if(evt.getKeyCode()==10){
            int cod = 0;
            try{
                cod = Integer.parseInt(txtCod.getText());
            }catch(NumberFormatException ex){
                
            }
            txtCod.setText("");
            txtValor.setText("");
            
            mostrar(cod);
            
        }
        
    }
    
    private void mostrar(int cod){
        String nome = "";
        String valor = "";
        int quant = Integer.parseInt(txtQuant.getText());
        boolean achou = true;
        try{
            
            DB db = new DB();
            db.getConnection();
            ResultSet res = db.readQuery("SELECT * FROM `produtos` where cod = '"+cod+"'");
            res.next();
            nome = res.getString("nome");
            valor = res.getString("valor");
            
        }catch(SQLException ex){
            achou = false;
        }
        if(achou){
            txtNome.setText(nome);
            txtValor.setText(valor+"");
            addLista(nome, Double.parseDouble(valor), quant);
        }else{
            JOptionPane.showMessageDialog(null, "Produto não cadastrado!");
        }
        
    }
    
    private void addLista(String nome, double valor, int quant){
        
        mod.addElement(nome+": Val. uni.: "+format.format(valor)+" Qunt.: "+quant+" Sub-total: "+format.format(valor*quant));
        unitVal.add(valor*quant);
        setValTotal();
        txtQuant.setText("1");
        
    }
    
    private void setValTotal(){
        valTotal = 0;
        for(double uni : unitVal){
            valTotal+=uni;
        }
        labTotal.setText(format.format(valTotal));
    }
}
