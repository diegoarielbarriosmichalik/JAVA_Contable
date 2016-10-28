package DEV;

import FORM.Clientes;
import FORM.Clientes_ABM;
import FORM.Compras;
import FORM.Compras_agregar_detalle;
import FORM.Compras_buscar_cuentas;
import FORM.Compras_proveedores_buscar;
import FORM.Comprobante;
import FORM.Condicion;
import FORM.Cuentas;
import FORM.Cuentas_ABM;
import FORM.Empresas;
import FORM.Empresas_ABM;
import FORM.Empresas_buscar_clientes;
import FORM.Moneda;
import FORM.Proveedores_ABM;
import FORM.Seleccionar_empresa;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Metodos {
    
    public static Connection conexion = null;
    public static int id_cuenta = 0;
    public static int id_cliente = 0;
    public static int id_proveedor = 0;
    public static int id_timbrado = 0;
    public static int id_condicion = 0;
    public static int id_comprobante = 0;
    public static int id_moneda = 0;
    public static int id_empresa = 0;
    public static int empresa = 0;
    public static String empresa_razon_social = "Selecciona una empresa...";
    public static String cuenta = null;
    
    public static void Iniciar_Conexion() {
        try {
            String db = null;
            String host = null;
            String user = null;
            String pass = null;
            
            db = "contable";
            host = "190.104.167.162"; // Host 4k
            user = "postgres";
            pass = "postgres";
            
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://" + host + ":5432/" + db, user, pass);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al iniciar la conexion con la base de datos." + ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Cliente_Guardar() {
        try {
            if (id_cliente == 0) {
                if ((Clientes_ABM.jTextField_nombre.getText().length() < 1)
                        || (Clientes_ABM.jTextField_ci.getText().length() < 1)
                        || (Clientes_ABM.jTextField_direccion.getText().length() < 1)
                        || (Clientes_ABM.jTextField_telefono.getText().length() < 1)
                        || (Clientes_ABM.jTextField_email.getText().length() < 1)) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                } else {
                    
                    Statement st1 = conexion.createStatement();
                    
                    ResultSet result = st1.executeQuery("SELECT MAX(id_propietario) FROM propietario");
                    if (result.next()) {
                        id_cliente = result.getInt(1) + 1;
                    }
                    
                    PreparedStatement stUpdateProducto = conexion.prepareStatement("INSERT INTO propietario VALUES(?,?,?,?,?,?,?)");
                    stUpdateProducto.setInt(1, id_cliente);
                    stUpdateProducto.setString(2, Clientes_ABM.jTextField_nombre.getText());
                    stUpdateProducto.setInt(3, Integer.parseInt(Clientes_ABM.jTextField_ci.getText()));
                    stUpdateProducto.setString(4, Clientes_ABM.jTextField_direccion.getText());
                    stUpdateProducto.setString(5, Clientes_ABM.jTextField_telefono.getText());
                    stUpdateProducto.setString(6, Clientes_ABM.jTextField_email.getText());
                    stUpdateProducto.setInt(7, 0);
                    stUpdateProducto.executeUpdate();
                    
                }
//            } else if (Clientes.jDateChooser_cumpleanos.getDate() != null) {
//                java.util.Date utilDate = Clientes.jDateChooser_cumpleanos.getDate();
//                java.sql.Date cumple = new java.sql.Date(utilDate.getTime());
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "cumpleanos = '" + cumple + "', "
//                        + "ci = '" + Integer.parseInt(Clientes.jTextField_ci.getText()) + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
////                    Clientes.jt_nombre.setEditable(false);
////                    JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
//
//            } else {
//                int ci = 0;
//                if (Clientes.jTextField_ci.getText().length() > 1) {
//                    ci = Integer.parseInt(Clientes.jTextField_ci.getText());
//                }
//
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "ci = '" + ci + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
            }
            
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
            
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public synchronized static void Timbrado_guardar() {
        try {
            if (id_proveedor != 0) {
                if ((Compras.jTextField_timbrado.getText().length() < 1)) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                } else {
                    boolean existe = false;
                    Statement st21 = conexion.createStatement();
                    ResultSet result2 = st21.executeQuery(""
                            + "SELECT * FROM timbrado "
                            + "where timbrado = '" + Compras.jTextField_timbrado.getText() + "' "
                            + "and id_proveedor = '" + id_proveedor + "'");
                    if (result2.next()) {
                        existe = true;
                    }
                    
                    if (existe == false) {
                        Statement st1 = conexion.createStatement();
                        
                        ResultSet result = st1.executeQuery("SELECT MAX(id_timbrado) FROM timbrado");
                        if (result.next()) {
                            id_timbrado = result.getInt(1) + 1;
                        }
                        
                        PreparedStatement stUpdateProducto = conexion.prepareStatement("INSERT INTO timbrado VALUES(?,?,?,?)");
                        stUpdateProducto.setInt(1, id_timbrado);
                        stUpdateProducto.setInt(2, Integer.parseInt(Compras.jTextField_timbrado.getText()));
                        stUpdateProducto.setDate(3, null);
                        stUpdateProducto.setInt(4, id_proveedor);
                        stUpdateProducto.executeUpdate();
                    }
                    
                }
//            } else if (Clientes.jDateChooser_cumpleanos.getDate() != null) {
//                java.util.Date utilDate = Clientes.jDateChooser_cumpleanos.getDate();
//                java.sql.Date cumple = new java.sql.Date(utilDate.getTime());
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "cumpleanos = '" + cumple + "', "
//                        + "ci = '" + Integer.parseInt(Clientes.jTextField_ci.getText()) + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
////                    Clientes.jt_nombre.setEditable(false);
////                    JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
//
//            } else {
//                int ci = 0;
//                if (Clientes.jTextField_ci.getText().length() > 1) {
//                    ci = Integer.parseInt(Clientes.jTextField_ci.getText());
//                }
//
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "ci = '" + ci + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
            }

            //  JOptionPane.showMessageDialog(null, "Guardado correctamente");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public synchronized static void Cuentas_guardar() {
        try {
            if (id_cuenta == 0) {
                if ((Cuentas_ABM.jTextField_nv1.getText().length() < 1)
                        || (Cuentas_ABM.jTextField_cuenta.getText().length() < 1)) {
                    System.err.println("Complete todos los campos");
                } else {
                    
                    Statement st1 = conexion.createStatement();
                    
                    ResultSet result = st1.executeQuery("SELECT MAX(id_cuenta) FROM cuenta");
                    if (result.next()) {
                        id_cuenta = result.getInt(1) + 1;
                    }
                    
                    PreparedStatement stUpdateProducto = conexion.prepareStatement("INSERT INTO cuenta VALUES(?,?,?,?,?,?,?,?)");
                    stUpdateProducto.setInt(1, id_cuenta);
                    stUpdateProducto.setString(2, Cuentas_ABM.jTextField_nv1.getText());
                    stUpdateProducto.setString(3, Cuentas_ABM.jTextField_nv2.getText());
                    stUpdateProducto.setString(4, Cuentas_ABM.jTextField_nv3.getText());
                    stUpdateProducto.setString(5, Cuentas_ABM.jTextField_nv4.getText());
                    stUpdateProducto.setString(6, Cuentas_ABM.jTextField_nv5.getText());
                    stUpdateProducto.setString(7, Cuentas_ABM.jTextField_cuenta.getText());
                    stUpdateProducto.setInt(8, 0);
                    stUpdateProducto.executeUpdate();
                    
                }
//            } else if (Clientes.jDateChooser_cumpleanos.getDate() != null) {
//                java.util.Date utilDate = Clientes.jDateChooser_cumpleanos.getDate();
//                java.sql.Date cumple = new java.sql.Date(utilDate.getTime());
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "cumpleanos = '" + cumple + "', "
//                        + "ci = '" + Integer.parseInt(Clientes.jTextField_ci.getText()) + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
////                    Clientes.jt_nombre.setEditable(false);
////                    JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
//
//            } else {
//                int ci = 0;
//                if (Clientes.jTextField_ci.getText().length() > 1) {
//                    ci = Integer.parseInt(Clientes.jTextField_ci.getText());
//                }
//
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "ci = '" + ci + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
            }
            
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
            
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public synchronized static void Proveedores_guardar() {
        try {
            if (id_proveedor == 0) {
                if ((Proveedores_ABM.jTextField_razon_social.getText().length() < 1)
                        || (Proveedores_ABM.jTextField_direccion.getText().length() < 1)
                        || (Proveedores_ABM.jTextField_ruc.getText().length() < 1)
                        || (Proveedores_ABM.jTextField_telefono.getText().length() < 1)) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                } else {
                    Statement st1 = conexion.createStatement();
                    
                    ResultSet result = st1.executeQuery("SELECT MAX(id_proveedor) FROM proveedor ");
                    if (result.next()) {
                        id_proveedor = result.getInt(1) + 1;
                    }
                    
                    PreparedStatement stUpdateProducto = conexion.prepareStatement("INSERT INTO proveedor VALUES(?,?,?,?,?,?,?)");
                    stUpdateProducto.setInt(1, id_proveedor);
                    stUpdateProducto.setString(2, Proveedores_ABM.jTextField_razon_social.getText());
                    stUpdateProducto.setString(3, Proveedores_ABM.jTextField_ruc.getText());
                    stUpdateProducto.setString(4, Proveedores_ABM.jTextField_telefono.getText());
                    stUpdateProducto.setString(5, Proveedores_ABM.jTextField_direccion.getText());
                    stUpdateProducto.setInt(6, 0); //borrado
                    stUpdateProducto.setInt(7, empresa);
                    stUpdateProducto.executeUpdate();
                    
                }

//            } else if (Clientes.jDateChooser_cumpleanos.getDate() != null) {
//                java.util.Date utilDate = Clientes.jDateChooser_cumpleanos.getDate();
//                java.sql.Date cumple = new java.sql.Date(utilDate.getTime());
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "cumpleanos = '" + cumple + "', "
//                        + "ci = '" + Integer.parseInt(Clientes.jTextField_ci.getText()) + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
////                    Clientes.jt_nombre.setEditable(false);
////                    JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
//
//            } else {
//                int ci = 0;
//                if (Clientes.jTextField_ci.getText().length() > 1) {
//                    ci = Integer.parseInt(Clientes.jTextField_ci.getText());
//                }
//
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "ci = '" + ci + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
            }
            
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
            
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public synchronized static void Empresa_guardar() {
        try {
            if (id_empresa == 0) {
                if ((id_cliente == 0)
                        || (Empresas_ABM.jTextField_razon_social.getText().length() < 1)
                        || (Empresas_ABM.jTextField_ruc.getText().length() < 1)
                        || (Empresas_ABM.jTextField_direccion.getText().length() < 1)
                        || (Empresas_ABM.jTextField_telefono.getText().length() < 1)) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                } else {
                    Statement st1 = conexion.createStatement();
                    ResultSet result = st1.executeQuery("SELECT MAX(id_empresa) FROM empresa");
                    if (result.next()) {
                        id_empresa = result.getInt(1) + 1;
                    }
                    PreparedStatement stUpdateProducto = conexion.prepareStatement("INSERT INTO empresa VALUES(?,?,?,?,?,?)");
                    stUpdateProducto.setInt(1, id_empresa);
                    stUpdateProducto.setString(2, Empresas_ABM.jTextField_razon_social.getText());
                    stUpdateProducto.setString(3, Empresas_ABM.jTextField_ruc.getText());
                    stUpdateProducto.setString(4, Empresas_ABM.jTextField_telefono.getText());
                    stUpdateProducto.setString(5, Empresas_ABM.jTextField_direccion.getText());
                    stUpdateProducto.setInt(6, id_cliente);
                    stUpdateProducto.executeUpdate();
                }
//            } else if (Clientes.jDateChooser_cumpleanos.getDate() != null) {
//                java.util.Date utilDate = Clientes.jDateChooser_cumpleanos.getDate();
//                java.sql.Date cumple = new java.sql.Date(utilDate.getTime());
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "cumpleanos = '" + cumple + "', "
//                        + "ci = '" + Integer.parseInt(Clientes.jTextField_ci.getText()) + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
////                    Clientes.jt_nombre.setEditable(false);
////                    JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
//
//            } else {
//                int ci = 0;
//                if (Clientes.jTextField_ci.getText().length() > 1) {
//                    ci = Integer.parseInt(Clientes.jTextField_ci.getText());
//                }
//
//                PreparedStatement st = conexion.prepareStatement(""
//                        + "UPDATE cliente "
//                        + "SET nombre ='" + jt_nombre.getText() + "', "
//                        + "direccion ='" + jt_direccion.getText() + "', "
//                        + "telefono ='" + jt_telefono.getText() + "', "
//                        + "ruc ='" + jt_ruc.getText() + "', "
//                        + "email = '" + jt_email.getText() + "', "
//                        + "ci = '" + ci + "' "
//                        + "WHERE id_cliente = '" + id_cliente + "'");
//                st.executeUpdate();
//                Clientes.jLabel_mensaje.setText("Actualizado correctamente");
//                Clientes.jLabel_mensaje.setVisible(true);
////                    Clientes.jt_nombre.requestFocus();
            }
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static void Compras_agregar_detalle_calculo_gravada10() {
        String gravada_10_str = Compras_agregar_detalle.jTextField_gravadas10.getText().replace(".", "");
        long gravada_10_long = Long.parseLong(gravada_10_str);
        long iva_10 = gravada_10_long / 11;
        gravada_10_long = gravada_10_long - iva_10;
        Compras_agregar_detalle.jTextField_gravadas10.setText(getSepararMiles(String.valueOf(gravada_10_long)));
        Compras_agregar_detalle.jTextField_iva10.setText(getSepararMiles(String.valueOf(iva_10)));
        long total = gravada_10_long + iva_10;
        Compras_agregar_detalle.jTextField_total.setText(getSepararMiles(String.valueOf(total)));
    }
    
    public synchronized static String getSepararMiles(String txtprec) {
        String valor = txtprec;
        
        int largo = valor.length();
        if (largo > 8) {
            valor = valor.substring(largo - 9, largo - 6) + "." + valor.substring(largo - 6, largo - 3) + "." + valor.substring(largo - 3, largo);
        } else if (largo > 7) {
            valor = valor.substring(largo - 8, largo - 6) + "." + valor.substring(largo - 6, largo - 3) + "." + valor.substring(largo - 3, largo);
        } else if (largo > 6) {
            valor = valor.substring(largo - 7, largo - 6) + "." + valor.substring(largo - 6, largo - 3) + "." + valor.substring(largo - 3, largo);
        } else if (largo > 5) {
            valor = valor.substring(largo - 6, largo - 3) + "." + valor.substring(largo - 3, largo);
        } else if (largo > 4) {
            valor = valor.substring(largo - 5, largo - 3) + "." + valor.substring(largo - 3, largo);
        } else if (largo > 3) {
            valor = valor.substring(largo - 4, largo - 3) + "." + valor.substring(largo - 3, largo);
        }
        txtprec = valor;
        return valor;
    }
    
    public static void Clientes_ABM_clear() {
        id_cliente = 0;
        Clientes_ABM.jTextField_nombre.setText("");
        Clientes_ABM.jTextField_ci.setText("");
        Clientes_ABM.jTextField_direccion.setText("");
        Clientes_ABM.jTextField_email.setText("");
        Clientes_ABM.jTextField_telefono.setText("");
        Clientes_ABM.jTextField_nombre.requestFocus();
        
    }
    
    public static void Proveedores_clear() {
        id_proveedor = 0;
        Proveedores_ABM.jTextField_direccion.setText("");
        Proveedores_ABM.jTextField_razon_social.setText("");
        Proveedores_ABM.jTextField_ruc.setText("");
        Proveedores_ABM.jTextField_telefono.setText("");
    }
    
    public static void Cuentas_clear() {
        id_cuenta = 0;
        Cuentas_ABM.jTextField_nv1.setText("");
        Cuentas_ABM.jTextField_nv2.setText("");
        Cuentas_ABM.jTextField_nv3.setText("");
        Cuentas_ABM.jTextField_nv4.setText("");
        Cuentas_ABM.jTextField_nv5.setText("");
        Cuentas_ABM.jTextField_cuenta.setText("");
    }
    
    public static void Cerrar_Conexion() {
        try {
            if (conexion != null) {
                conexion.close();
                System.err.println("Conexion finalizada");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public static void Verificar_conexion() {
        try {
            if (conexion.isClosed() == false) {
                System.err.println("conexion OK");
            } else {
                System.err.println("conexion FAIL");
                Iniciar_Conexion();
                if (conexion.isClosed() == false) {
                    System.err.println("conexion reiniciada; Conexion OK");
                } else {
                    JOptionPane.showMessageDialog(null, "Error de conexion con la base de datos.");
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Cuentas_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Cuentas.jTable1.getModel();
            for (int j = 0; j < Cuentas.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_cuenta,  (nv1 || '.' || nv2|| '.' || nv3|| '.' || nv4|| '.' || nv5|| ' ' || cuenta) AS cuenta  "
                    + "from cuenta "
                    + "where cuenta ilike '%" + Cuentas.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Cuentas.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    public synchronized static void Condicion_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Condicion.jTable1.getModel();
            for (int j = 0; j < Condicion.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_condicion,  condicion  "
                    + "from condicion ");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Condicion.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    public synchronized static void Comprobante_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Comprobante.jTable1.getModel();
            for (int j = 0; j < Comprobante.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_comprobante,  comprobante  "
                    + "from comprobante ");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Comprobante.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    public synchronized static void Moneda_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Moneda.jTable1.getModel();
            for (int j = 0; j < Moneda.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_moneda,  moneda  "
                    + "from moneda ");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Moneda.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Compras_proveedores_buscar_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Compras_proveedores_buscar.jTable1.getModel();
            for (int j = 0; j < Compras_proveedores_buscar.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_proveedor, nombre, ruc, telefono "
                    + "from proveedor "
                    + "where nombre ilike '%" + Compras_proveedores_buscar.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Compras_proveedores_buscar.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Seleccionar_empresas_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Seleccionar_empresa.jTable1.getModel();
            for (int j = 0; j < Seleccionar_empresa.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_empresa, razon_social, id_propietario  "
                    + "from empresa");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Seleccionar_empresa.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Empresas_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Empresas.jTable1.getModel();
            for (int j = 0; j < Empresas.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_empresa, razon_social, id_propietario  "
                    + "from empresa "
                    + "where razon_social ilike '%" + Empresas.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Empresas.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Clientes_buscar_cliente_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Clientes.jTable1.getModel();
            for (int j = 0; j < Clientes.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_propietario, nombre, ci, telefono  "
                    + "from propietario "
                    + "where nombre ilike '%" + Clientes.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Clientes.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Empresas_clientes_buscar_cliente_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Empresas_buscar_clientes.jTable1.getModel();
            for (int j = 0; j < Empresas_buscar_clientes.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_propietario, nombre, ci, telefono  "
                    + "from propietario "
                    + "where nombre ilike '%" + Empresas_buscar_clientes.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Empresas_buscar_clientes.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Compras_cuentas_cargar_jtable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) Compras_buscar_cuentas.jTable1.getModel();
            for (int j = 0; j < Compras_buscar_cuentas.jTable1.getRowCount(); j++) {
                dtm.removeRow(j);
                j -= 1;
            }
            PreparedStatement ps2 = conexion.prepareStatement(""
                    + "select id_cuenta,  (nv1 || '.' || nv2|| '.' || nv3|| '.' || nv4|| '.' || nv5|| ' ' || cuenta) AS cuenta  "
                    + "from cuenta "
                    + "where cuenta ilike '%" + Compras_buscar_cuentas.jTextField_buscar.getText() + "%'");
            ResultSet rs2 = ps2.executeQuery();
            ResultSetMetaData rsm = rs2.getMetaData();
            ArrayList<Object[]> data2 = new ArrayList<>();
            while (rs2.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rs2.getObject(i + 1).toString().trim();
                }
                data2.add(rows);
            }
            dtm = (DefaultTableModel) Compras_buscar_cuentas.jTable1.getModel();
            for (int i = 0; i < data2.size(); i++) {
                dtm.addRow(data2.get(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized static void Compras_buscar_cuentas_seleccionar_cuenta() {
        
        DefaultTableModel tm = (DefaultTableModel) Compras_buscar_cuentas.jTable1.getModel();
        id_cuenta = Integer.parseInt(String.valueOf(tm.getValueAt(Compras_buscar_cuentas.jTable1.getSelectedRow(), 0)));
        
        try {
            Statement ST = conexion.createStatement();
            ResultSet RS = ST.executeQuery("SELECT * FROM cuenta where id_cuenta = '" + id_cuenta + "'");
            if (RS.next()) {
                cuenta = RS.getString("cuenta").trim();
                Compras.jTextField_cuenta.setText(RS.getString("cuenta").trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    
    public synchronized static void Empresas_cliente_seleccionar() {
        DefaultTableModel tm = (DefaultTableModel) Empresas_buscar_clientes.jTable1.getModel();
        id_cliente = Integer.parseInt(String.valueOf(tm.getValueAt(Empresas_buscar_clientes.jTable1.getSelectedRow(), 0)));
        Empresas_ABM.jTextField_cliente.setText(String.valueOf(tm.getValueAt(Empresas_buscar_clientes.jTable1.getSelectedRow(), 1)));
    }
    
    public synchronized static void Condicion_selecionar() {
        DefaultTableModel tm = (DefaultTableModel) Condicion.jTable1.getModel();
        id_condicion = Integer.parseInt((String.valueOf(tm.getValueAt(Condicion.jTable1.getSelectedRow(), 0))));
        Compras.jTextField_condicion.setText(String.valueOf(tm.getValueAt(Condicion.jTable1.getSelectedRow(), 1)));
    }
    public synchronized static void Comprobante_selecionar() {
        DefaultTableModel tm = (DefaultTableModel) Comprobante.jTable1.getModel();
        id_comprobante = Integer.parseInt((String.valueOf(tm.getValueAt(Comprobante.jTable1.getSelectedRow(), 0))));
        Compras.jTextField_comprobante.setText(String.valueOf(tm.getValueAt(Comprobante.jTable1.getSelectedRow(), 1)));
    }
    
    public synchronized static void Moneda_selecionar() {
        DefaultTableModel tm = (DefaultTableModel) Moneda.jTable1.getModel();
        id_moneda = Integer.parseInt((String.valueOf(tm.getValueAt(Moneda.jTable1.getSelectedRow(), 0))));
        Compras.jTextField_moneda.setText(String.valueOf(tm.getValueAt(Moneda.jTable1.getSelectedRow(), 1)));
    }
    
    public synchronized static void Seleccionar_empresa() {
        DefaultTableModel tm = (DefaultTableModel) Seleccionar_empresa.jTable1.getModel();
        empresa = Integer.parseInt(String.valueOf(tm.getValueAt(Seleccionar_empresa.jTable1.getSelectedRow(), 0)));
        empresa_razon_social = "Empresa activa: " + String.valueOf(tm.getValueAt(Seleccionar_empresa.jTable1.getSelectedRow(), 1));
    }
    
    public synchronized static void Cuentas_seleccionar() {
        DefaultTableModel tm = (DefaultTableModel) Cuentas.jTable1.getModel();
        id_cuenta = Integer.parseInt(String.valueOf(tm.getValueAt(Cuentas.jTable1.getSelectedRow(), 0)));
        
        try {
            Statement ST = conexion.createStatement();
            ResultSet RS = ST.executeQuery("SELECT * FROM cuenta where id_cuenta = '" + id_cuenta + "'");
            if (RS.next()) {
                Cuentas_ABM.jTextField_nv1.setText(RS.getString("nv1"));
                Cuentas_ABM.jTextField_nv2.setText(RS.getString("nv2"));
                Cuentas_ABM.jTextField_nv3.setText(RS.getString("nv3"));
                Cuentas_ABM.jTextField_nv4.setText(RS.getString("nv4"));
                Cuentas_ABM.jTextField_nv5.setText(RS.getString("nv5"));
                Cuentas_ABM.jTextField_cuenta.setText(RS.getString("cuenta").trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    
    public synchronized static void Compras_proveedores_selecionar() {
        DefaultTableModel tm = (DefaultTableModel) Compras_proveedores_buscar.jTable1.getModel();
        id_proveedor = Integer.parseInt(String.valueOf(tm.getValueAt(Compras_proveedores_buscar.jTable1.getSelectedRow(), 0)));
        try {
            Statement ST = conexion.createStatement();
            ResultSet RS = ST.executeQuery(""
                    + "SELECT * FROM proveedor "
                    + "where id_proveedor = '" + id_proveedor + "'");
            if (RS.next()) {
                
                Compras.jTextField_proveedor.setText(RS.getString("nombre"));
                Compras.jTextField_ruc.setText(RS.getString("ruc"));
                
                Statement ST2 = conexion.createStatement();
                ResultSet RS2 = ST2.executeQuery(""
                        + "SELECT * FROM timbrado "
                        + "where id_proveedor = '" + id_proveedor + "' order by vencimiento DESC");
                if (RS2.next()) {
                    Compras.jTextField_timbrado.setText(RS2.getString("timbrado"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
