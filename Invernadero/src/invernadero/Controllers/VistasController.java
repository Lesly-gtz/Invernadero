package invernadero.Controllers;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.StageStyle;

/**
 *
 * @author Lesly
 */
public class VistasController implements Initializable{
    ObservableList<String> manejadores = FXCollections.observableArrayList("MySQL", "SQLServer");
    ObservableList<String> reporte = FXCollections.observableArrayList("Cantidad de Productos por Tipo de Producto", "Cantidad de Riegos por Día",
                                                                        "Cantidad de Productos por Condición Actual", "Cantidad de Fotografías por Tipo de Producto");
    ObservableList<String> condicionA = FXCollections.observableArrayList("Buen Estado", "Falta de Agua", "Exceso de Agua", "Con Plaga");
    
    /*LOGIN*/
    @FXML private AnchorPane login;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboBox;
    /*MENU*/
    @FXML private AnchorPane menu;
    @FXML private ComboBox<String> report;
    @FXML 
    private MediaView mv;
    private MediaPlayer mp;
    private Media me;
    /*TIPO PRODUCTOS*/
    @FXML private AnchorPane tipoProductos;
    @FXML private TextField txtTipo;
    @FXML private TextField txtCodigoTipo;
    @FXML private TextField txtId;
    @FXML private TableView<TipoProductos> tablaTipo;
    @FXML private TableColumn<TipoProductos, String> codigoT;
    @FXML private TableColumn<TipoProductos, String> tipo;
    private ObservableList<TipoProductos>listaTipo;
    /*PRODUCTOS*/
    @FXML private AnchorPane productos;
    @FXML private TextField txtIdP;
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtProducto;
    @FXML private DatePicker dateProducto;
    @FXML private ComboBox<String> condicionP;
    @FXML private ComboBox<TipoProductos> tipoP;
    private ObservableList<TipoProductos> combo;
    private ObservableList<Productos> listaProductos;
    @FXML private TableView<Productos> tablaProducto;
    @FXML private TableColumn<Productos, String> codigoP;
    @FXML private TableColumn<Productos, String> producto;
    @FXML private TableColumn<Productos, TipoProductos> codigoTipo;
    @FXML private TableColumn<Productos, Date> fecha;
    @FXML private TableColumn<Productos, String> estado;
    /*RIEGO*/
    @FXML private AnchorPane riego;
    @FXML private ComboBox<Productos> cbProducto;
    private ObservableList<Productos> comboRiego;
    private ObservableList<Riego> listaRiego;
    @FXML private TextField txtIdR;
    @FXML private DatePicker dateRiego;
    @FXML private TableView<Riego> tablaRiego;
    @FXML private TableColumn<Riego, String> codigoRiego;
    @FXML private TableColumn<Riego, Productos> productoR;
    @FXML private TableColumn<Riego, Date> diaRiego;
    
    @FXML private AnchorPane historial;
    @FXML private AnchorPane reportes;
    
    
    
    Conexion conexion;
    /*CONEXION A BASE DE DATOS*/
    @FXML
    void btnSesion (ActionEvent event){   
        String cb = comboBox.getValue();
        conexion = new Conexion();
        if(cb.equals("MySQL")){
            conexion.setUsername(txtUsername.getText());
            conexion.setPassword(txtPassword.getText());
            conexion.setDriver("com.mysql.jdbc.Driver");
            conexion.setUrlBD("jdbc:mysql://localhost:3306/invernadero");
            if(conexion.agregarConexion() == null){
                Alert dialogo = new Alert(AlertType.ERROR);
                dialogo.setTitle("ERROR");
                dialogo.setHeaderText("Error de conexión");
                dialogo.setContentText("Usuario ó contraseña incorrectos");
                dialogo.initStyle(StageStyle.UTILITY);
                dialogo.showAndWait();
                
            }
            else{
                Alert dialogo = new Alert(AlertType.INFORMATION);
                dialogo.setTitle("CONFIRMACIÓN");
                dialogo.setHeaderText("Conectado");
                dialogo.setContentText("Iniciando Sesión...");
                dialogo.initStyle(StageStyle.UTILITY);
                dialogo.showAndWait();
                this.menu.setVisible(true);
                this.login.setVisible(false);
            }       
        }
        else if(cb.equals("SQLServer")){
            conexion.setUsername(txtUsername.getText());
            conexion.setPassword(txtPassword.getText());
            conexion.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexion.setUrlBD("jdbc:sqlserver://localhost:1433;databaseName=invernadero");
            if(conexion.agregarConexion() == null){
                Alert dialogo = new Alert(AlertType.ERROR);
                dialogo.setTitle("ERROR");
                dialogo.setHeaderText("Error de conexión");
                dialogo.setContentText("Usuario ó contraseña incorrectos");
                dialogo.initStyle(StageStyle.UTILITY);
                dialogo.showAndWait();
                
            }
            else{
                Alert dialogo = new Alert(AlertType.INFORMATION);
                dialogo.setTitle("CONFIRMACIÓN");
                dialogo.setHeaderText("Conectado");
                dialogo.setContentText("Iniciando Sesión...");
                dialogo.initStyle(StageStyle.UTILITY);
                dialogo.showAndWait();
                this.menu.setVisible(true);
                this.login.setVisible(false);
            }
        }
        /*VIDEO*/
        String path = new File("src/img/presentacion.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mv.setFitWidth(530);
        mv.setFitHeight(500);
    }
    
    /*CRUD TIPO PRODUCTO*/
    @FXML
    void btnTipoProducto(ActionEvent event){
        this.tipoProductos.setVisible(true);
        this.menu.setVisible(false);
        this.riego.setVisible(false);
        this.historial.setVisible(false);
        this.reportes.setVisible(false);
        this.productos.setVisible(false);
        listaTipo = FXCollections.observableArrayList();
        codigoT.setCellValueFactory(new PropertyValueFactory<>("codigoTipo"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipoProducto")); 
        TipoProductos.llenarTabla(conexion.agregarConexion(), listaTipo);
        tablaTipo.setItems(listaTipo);
        gestionarEventosT();
    }
    @FXML
    void btnGuardarT(ActionEvent event){
        TipoProductos tp = new TipoProductos(0, txtCodigoTipo.getText(),txtTipo.getText());
        int resultado = tp.agregarTipo(conexion.agregarConexion());
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Inserción");
            dialogo.setContentText("Se inserto correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaTipo.add(tp);
        }
        else{
            Alert dialogo = new Alert(AlertType.ERROR);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText("Error de Inserción");
            dialogo.setContentText("No se pudo insertar");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    @FXML void btnEditarT(ActionEvent event){
        TipoProductos tp = new TipoProductos(Integer.valueOf(txtId.getText()), txtCodigoTipo.getText(),txtTipo.getText());
        int resultado = tp.actualizarTipo(conexion.agregarConexion());        
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Actualización");
            dialogo.setContentText("Se actualizo correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaTipo.set(tablaTipo.getSelectionModel().getSelectedIndex(), tp);
        }
        else{
            Alert dialogo = new Alert(AlertType.ERROR);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText("Error de Actualización");
            dialogo.setContentText("No se pudo actualizar");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    @FXML void btnEliminarT(ActionEvent event){
        int resultado = tablaTipo.getSelectionModel().getSelectedItem().eliminarTipo(conexion.agregarConexion());
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Eliminación");
            dialogo.setContentText("Se elimino correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaTipo.remove(tablaTipo.getSelectionModel().getSelectedIndex());
        }
        
    }
    /*CRUD PRODUCTOS*/
    @FXML 
    void btnProductos(ActionEvent event){
        this.productos.setVisible(true);
        this.tipoProductos.setVisible(false);
        this.riego.setVisible(false);
        this.historial.setVisible(false);
        this.reportes.setVisible(false);
        this.menu.setVisible(false);
        combo = FXCollections.observableArrayList();
        TipoProductos.llenarCombo(conexion.agregarConexion(), combo);
        tipoP.setItems(combo);
        listaProductos = FXCollections.observableArrayList();
        codigoP.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        producto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        codigoTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        estado.setCellValueFactory(new PropertyValueFactory<>("condicion"));
        Productos.llenarTablaP(conexion.agregarConexion(), listaProductos);
        tablaProducto.setItems(listaProductos);
        gestionarEventosP();
    }
    @FXML void btnGuardarP(ActionEvent event){
        Productos p = new Productos(0, txtCodigoProducto.getText(), txtProducto.getText(),tipoP.getSelectionModel().getSelectedItem(),Date.valueOf(dateProducto.getValue()), condicionP.getSelectionModel().getSelectedItem());
        int resultado = p.agregarProducto(conexion.agregarConexion());
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Inserción");
            dialogo.setContentText("Se inserto correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaProductos.add(p);
        }
        else{
            Alert dialogo = new Alert(AlertType.ERROR);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText("Error de Inserción");
            dialogo.setContentText("No se pudo insertar");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    @FXML void btnEditarP(ActionEvent event){
        Productos p = new Productos(Integer.valueOf(txtIdP.getText()), txtCodigoProducto.getText(), txtProducto.getText(),tipoP.getSelectionModel().getSelectedItem(),Date.valueOf(dateProducto.getValue()), condicionP.getSelectionModel().getSelectedItem());
        int resultado = p.actualizarProducto(conexion.agregarConexion());
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Actualización");
            dialogo.setContentText("Se actualizo correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaProductos.set(tablaProducto.getSelectionModel().getSelectedIndex(), p);
        }
        else{
            Alert dialogo = new Alert(AlertType.ERROR);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText("Error de Actualización");
            dialogo.setContentText("No se pudo actualizar");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    @FXML void btnEliminarP(ActionEvent event){
        int resultado = tablaProducto.getSelectionModel().getSelectedItem().eliminarProducto(conexion.agregarConexion());
        if(resultado == 1){
            Alert dialogo = new Alert(AlertType.INFORMATION);
            dialogo.setTitle("CONFIRMACIÓN");
            dialogo.setHeaderText("Verificación de Eliminación");
            dialogo.setContentText("Se elimino correctamente");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait(); 
            listaProductos.remove(tablaProducto.getSelectionModel().getSelectedIndex());
        }
    }  
    /*CRUD RIEGO*/
    @FXML
    void btnRiego(ActionEvent event){
        this.riego.setVisible(true);
        this.menu.setVisible(false);
        this.tipoProductos.setVisible(false);
        this.productos.setVisible(false);
        this.historial.setVisible(false);
        this.reportes.setVisible(false);
        comboRiego = FXCollections.observableArrayList();
        Productos.llenarComboP(conexion.agregarConexion(), comboRiego);
        cbProducto.setItems(comboRiego);
    }
    @FXML void btnGuardarR(ActionEvent event){
        
    }
    @FXML void btnEditarR(ActionEvent event){
        
    }
    @FXML void btnEliminarR(ActionEvent event){
        
    }
    
    @FXML
    void btnHistorial(ActionEvent event){
        this.historial.setVisible(true);
        this.menu.setVisible(false);
        this.tipoProductos.setVisible(false);
        this.productos.setVisible(false);
        this.riego.setVisible(false);
        this.reportes.setVisible(false);
    }   
    
    @FXML
    void btnReporte(ActionEvent event){
        this.reportes.setVisible(true);
        this.historial.setVisible(false);
        this.menu.setVisible(false);
        this.tipoProductos.setVisible(false);
        this.productos.setVisible(false);
        this.riego.setVisible(false);
    }
    
    @FXML
    void btnStop(ActionEvent event){
        mp.seek(mp.getTotalDuration());
        mp.stop();
    }
    
    @FXML
    void btnPlay(ActionEvent event){
        mp.play();
    }
    
    @FXML
    void btnPause(ActionEvent event){
        mp.pause();
    }
    
    @FXML
    void btnInicio(ActionEvent event){
        this.menu.setVisible(true);
        this.tipoProductos.setVisible(false);
        this.productos.setVisible(false);
        this.historial.setVisible(false);
        this.reportes.setVisible(false);
        this.riego.setVisible(false);

    }
    
    /*OBTENER DATOS DE LA TABLA*/
    public void gestionarEventosT(){
        tablaTipo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TipoProductos>() {
            @Override
            public void changed(ObservableValue<? extends TipoProductos> observable, TipoProductos valorAnterior, TipoProductos valorSeleccionado) {
                if(valorSeleccionado != null){
                    txtId.setText(String.valueOf(valorSeleccionado.getId()));
                    txtCodigoTipo.setText(String.valueOf(valorSeleccionado.getCodigoTipo()));
                    txtTipo.setText(String.valueOf(valorSeleccionado.getTipoProducto()));
                }
            }
        });
    }
    
    /*OBTENER DATOS DE LA TABLA PRDUCTOS*/
    public void gestionarEventosP(){
        tablaProducto.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Productos>() {
            @Override
            public void changed(ObservableValue<? extends Productos> observable, Productos valorAnterior, Productos valorSeleccionado) {
                if(valorSeleccionado != null){
                    txtIdP.setText(String.valueOf(valorSeleccionado.getId()));
                    txtCodigoProducto.setText(String.valueOf(valorSeleccionado.getCodigoProducto()));
                    txtProducto.setText(String.valueOf(valorSeleccionado.getNombreProducto()));
                    tipoP.setValue(valorSeleccionado.getTipo());
                    dateProducto.setValue(valorSeleccionado.getFecha().toLocalDate());
                    condicionP.setValue(valorSeleccionado.getCondicion());
                }
            }
        });
    }
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // condicion = new ComboBox<>();
        comboBox.setItems(manejadores);
        report.setItems(reporte);
        condicionP.setItems(condicionA);
    }
}
