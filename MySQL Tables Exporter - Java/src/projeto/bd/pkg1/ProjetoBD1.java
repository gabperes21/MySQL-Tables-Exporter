package projeto.bd.pkg1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.util.Date;
import java.util.Scanner;


public class ProjetoBD1 {
    
   private BufferedWriter fw;
   public static void main(String args[]) throws SQLException, IOException{
    
    ProjetoBD1 file = new ProjetoBD1(); 
    Scanner s = new Scanner(System.in);
    
    System.out.println("Digite o servidor de banco de dados:");
    String servidor = s.next();
    System.out.println("Digite o usuario:");
    String usuario = s.next();
    System.out.println("Digite o senha:");
    String senha = s.next();
    System.out.println("Digite o banco de dados:");
    String bd = s.next();
    System.out.println("Digite o tabela:");
    String tabela = s.next();
    
    
    file.exportTab(servidor, usuario, senha, bd, tabela);
    
   }
   
   public void exportTab(String servidor, String usuario, String senha, String bd, String tabela) throws SQLException, IOException{
      
      String url, sql, file, auxStr;
      Statement st;
      ResultSet res;
      
      int countCol;
      Object auxObj;
      url = "jdbc:mysql://" + servidor + "/" + bd;
      file = bd+"-"+tabela+".csv";
      
      try(Connection conexao = DriverManager.getConnection(url, usuario, senha)){
          sql = "SELECT * FROM " + tabela;
          st = conexao.createStatement();
          res = st.executeQuery(sql);
          
          fw = new BufferedWriter(new FileWriter(file));
          
          countCol = lerCol(res);
          
          while (res.next()){
              String linha = "";
              //System.out.println(res.getString("dept_no"));
              
              for(int i = 1; i<= countCol; i++){
                  auxObj = res.getObject(i);
                  auxStr = "";
                  
                  if(auxStr != null){
                    auxStr = auxObj.toString();
                  }
                  if(auxObj instanceof String) {
                    auxStr = "\"" + arrumarAspas(auxStr) + "\"";                  
                  }                
                  linha = linha.concat(auxStr);
                  
                  if(i != countCol){
                    linha = linha.concat(",");
                  }                  
              }

            
            fw.newLine();            
            fw.write(linha);  
            fw.flush();
          }     
                   
      } catch(SQLException e){
          System.out.println("Problema com o BD: ");
          e.printStackTrace();
      } catch(IOException e){
          System.out.println("Problema com o Arquivo: ");
          e.printStackTrace();
      }
     
      
}
    private int lerCol(ResultSet res) throws SQLException, IOException {
        ResultSetMetaData data = res.getMetaData();
        int numCol = data.getColumnCount();
        String linha = "";
        String nomeCol;
        
        for(int i = 1; i<= numCol; i++){
            nomeCol = data.getColumnName(i);
            linha = linha.concat(nomeCol).concat(",");
        }
        fw.write(linha.substring(0, linha.length() - 1));
        return numCol;
    }

    private String arrumarAspas(String auxStr) {
        String escapedData = auxStr.replaceAll("\\R", " ");
        if (auxStr.contains(",") || auxStr.contains("\"") || auxStr.contains("'")){
            auxStr = auxStr.replace("\"", "\"\"");
            escapedData = "\"" + auxStr + "\"";
    } 
        return escapedData; 
    }

}
