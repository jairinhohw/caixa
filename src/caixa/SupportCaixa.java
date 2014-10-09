
package caixa;

import impressora.Comandos;
import impressora.Imp;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    
    public void fecharCompra(){
        Double recebido = Double.NaN;
        Timestamp data = new Timestamp(System.currentTimeMillis());
        String desc = "";
        boolean erro = false;
        double val_total = 0;
        
        while(recebido.equals(Double.NaN)){
            try{
                recebido = Double.parseDouble(JOptionPane.showInputDialog("Dinheiro recebido"));
            }catch(NumberFormatException ex){}
        }
        setValTotal();
        JOptionPane.showMessageDialog(null, "Troco:\n"+format.format(recebido-valTotal));
        
        val_total = valTotal;
        int i = mod.getSize();
        int x = 0;
        while(x<i){
            desc += mod.elementAt(x)+"\n";
            x++;
        }
        try {
            new DB().writeQuery("insert into historico values(null,'"
                    + data.toString() + "','"
                    + desc + "','"
                    + val_total + "')");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados!", "Erro:", JOptionPane.ERROR_MESSAGE);
            erro = true;
        }
        if(!erro){
            limpar();
            
            String imprimir = "";
            
            imprimir +=(Comandos.sublinhadoOn+"Sorvetes Wiethan"+Comandos.sublinhadoOff+"\n");
            imprimir +=("Fone: (55) 3025-2603"+"\n");
            imprimir +=("Rua Armando Ricardo Wiethan, n: 5016"+"\n");
            imprimir +=("Passo da Ferreira, Santa Maria, RS"+"\n");
            imprimir +=("CNPJ: 13.014.624/0001-00"+"\n");
            imprimir +=("\n");
            
            DateFormat d = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            imprimir +=(d.format((new Date(System.currentTimeMillis())))+"\n");
            
            imprimir +=("\n");
            imprimir +=(desc+"\n");
            imprimir +=("\n");
            imprimir +=("Total: "+val_total+"\n");
            imprimir +=("Recebido: "+recebido+"\n");
            imprimir +=("Troco: "+(-val_total+recebido)+"\n\n");
            imprimir +=(Comandos.negritoOn+" ESTE NAO E UM DOCUMENTO FISCAL "+"\n");
            imprimir +=("\n\n\n\n");
            imprimir +=(Comandos.cortar);
            imprimir +=(Comandos.abreGaveta);
            
            Imp.imprime(imprimir);
            
        }
    }
    
    public void limpar(){
        txtCod.setText("");
        txtNome.setText("");
        txtQuant.setText("1");
        txtValor.setText("");
        labTotal.setText("0,00");
        
        valTotal = 0;
        mod.clear();
        unitVal.clear();
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
