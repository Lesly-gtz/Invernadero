package invernadero.Controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Lesly
 */
public class Productos {
   private IntegerProperty id;
   private StringProperty codigoProducto;
   private StringProperty nombreProducto;
   private Date fecha;
   private TipoProductos tipo;
   private StringProperty condicion;
   
   public Productos(){}
   
   public Productos(Integer id, String codigoProducto, String nombreProducto, TipoProductos tipo, Date fecha, String condicion){
       this.id = new SimpleIntegerProperty(id);
       this.codigoProducto = new SimpleStringProperty(codigoProducto);
       this.nombreProducto = new SimpleStringProperty(nombreProducto);
       this.tipo = tipo;
       this.fecha = fecha;
       this.condicion = new SimpleStringProperty(condicion); 
   }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty (id);
    }

    public String getCodigoProducto() {
        return codigoProducto.get();
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = new SimpleStringProperty(codigoProducto);
    }

    public String getNombreProducto() {
        return nombreProducto.get();
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoProductos getTipo() {
        return tipo;
    }

    public void setTipo(TipoProductos tipo) {
        this.tipo = tipo;
    }

    public String getCondicion() {
        return condicion.get();
    }

    public void setCondicion(String condicion) {
        this.condicion = new SimpleStringProperty(condicion);
    }
       
    public int agregarProducto(Connection connection){
        PreparedStatement ps;
       try {
           ps = connection.prepareStatement("INSERT INTO productos(codigoP, nombreP, tipo, fecha, condicion) VALUES(?,?,?,?,?)");
           ps.setString(1, codigoProducto.get());
           ps.setString(2, nombreProducto.get());
           ps.setString(3, tipo.getTipoProducto());
           ps.setDate(4, fecha);
           ps.setString(5, condicion.get());
            return ps.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
           return 0;
       }
    }
    
    public int actualizarProducto(Connection connection){
       try {
           PreparedStatement ps = connection.prepareStatement("UPDATE productos SET codigoP = ?, nombreP = ?, tipo = ?, fecha = ?, condicion = ? WHERE id = ?");
           ps.setString(1, codigoProducto.get());
           ps.setString(2, nombreProducto.get());
           ps.setString(3, tipo.getTipoProducto());
           ps.setDate(4, fecha);
           ps.setString(5, condicion.get());
           ps.setInt(6, id.get());
           return ps.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
           return 0;
       }
    }
    
    public int eliminarProducto(Connection connection){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM productos WHERE id=?");
            ps.setInt(1, id.get());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public static void llenarTablaP(Connection connection, ObservableList<Productos> lista){
       try {
           Statement st =connection.createStatement();
           ResultSet rs = st.executeQuery("SELECT * FROM productos INNER JOIN tipo ON (tipo = tipo_producto)");
           while(rs.next()){
               lista.add(new Productos(rs.getInt("id"), rs.getString("codigoP"), rs.getString("nombreP"), new TipoProductos(rs.getInt("id"), rs.getString("codigo_tipo"), rs.getString("tipo_producto")), rs.getDate("fecha"),rs.getString("condicion")));
           }
       } catch (SQLException ex) {
           Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    public static void llenarComboP(Connection connection, ObservableList<Productos> combo){
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM productos");
            while(rs.next()){
                combo.add(new Productos(rs.getInt("id"), rs.getString("codigoP"), rs.getString("nombreP"), new TipoProductos(rs.getInt("id"), rs.getString("codigo_tipo"), rs.getString("tipo_producto")), rs.getDate("fecha"),rs.getString("condicion")));
            }
        }
        catch(SQLException e){
            
        }
    }
     
    @Override
    public String toString() {
        return nombreProducto.get();
    }
  
}
