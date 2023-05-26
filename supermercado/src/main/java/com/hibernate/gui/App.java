package com.hibernate.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.IntelliJTheme;
import com.hibernate.dao.CategoriaDAO;
import com.hibernate.dao.ProductoDAO;
import com.hibernate.model.Categoria;
import com.hibernate.model.Producto;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class App {

	private JFrame frmSupermercado;
	private JTextField tfIdProducto;
	private JTextField tfNombreProducto;
	private JTextField tfPrecioProducto;
	private JTextField tfUnidadesProducto;
	private JComboBox cbCategoria;
	private JComboBox cbCategoriaFiltro;
	private JComboBox cbOfertas;
	private String rutaFoto;
	private JButton btnEliminarProducto;
	private JButton btnActualizarProducto;
	private JButton btnInsertarProducto;
	private JLabel lblCategoria;
	private JLabel lblUnidades;
	private JLabel lblPrecio;
	private JLabel lblNombreProducto;
	private JLabel lblIdProducto;
	private JScrollPane scrollPane;
	private JTable tableProducto;
	private JLabel lblHomeSupermercado;
	private JMenuBar menuBar;

	/**
	 * Se encarga de obtener la extensión de las imágenes subidas.
	 */

	public String getFileExtension(File file) {
		String extension = "";
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			extension = fileName.substring(dotIndex + 1).toLowerCase();
		}
		return extension;
	}

	public void refrescarTabla(DefaultTableModel modelProducto, ProductoDAO productoDAO, CategoriaDAO categoriaDAO) {
		List<Producto> productos = null;
		List<Categoria> categorias = null;
		int idCategoria;
		switch (cbCategoriaFiltro.getSelectedIndex()) {
		case 0:
			idCategoria = 1000;
			break;
		case 1:
			idCategoria = 1001;
			break;
		case 2:
			idCategoria = 1;
			break;
		case 3:
			idCategoria = 2;
			break;
		case 4:
			idCategoria = 3;
			break;
		case 5:
			idCategoria = 4;
			break;
		case 6:
			idCategoria = 5;
			break;
		case 7:
			idCategoria = 6;
			break;
		case 8:
			idCategoria = 7;
			break;
		case 9:
			idCategoria = 8;
			break;
		case 10:
			idCategoria = 9;
			break;
		case 11:
			idCategoria = 10;
			break;
		default:
			idCategoria = 10;
			break;
		}

		modelProducto.setRowCount(0);

		if (idCategoria == 1000) {
			productos = productoDAO.selectAllProducto();
		} else if (idCategoria == 1001) {
			productos = productoDAO.selectProductosSinStock();
		} else {
			productos = productoDAO.selectProductosByIdCategoria(idCategoria);
		}
		categorias = categoriaDAO.selectAllCategoria();

		for (Producto p : productos) {
			Object[] row = new Object[5];

			row[0] = p.getIdProducto();
			row[1] = p.getNombreProducto();
			switch (cbOfertas.getSelectedIndex()) {
			case 0:
				row[2] = p.getPrecioProducto();
				break;
			case 1:
				double precioInicial25 = p.getPrecioProducto();
				double precioRebajado25 = (p.getPrecioProducto() * 25) / 100;
				row[2] = String.format("%.2f", (precioInicial25 - precioRebajado25));
				break;
			case 2:
				double precioInicial50 = p.getPrecioProducto();
				double precioRebajado50 = (p.getPrecioProducto() * 50) / 100;
				row[2] = String.format("%.2f", (precioInicial50 - precioRebajado50));
				break;
			case 3:
				double precioInicial75 = p.getPrecioProducto();
				double precioRebajado75 = (p.getPrecioProducto() * 75) / 100;
				row[2] = String.format("%.2f", (precioInicial75 - precioRebajado75));
				break;
			case 4:
				double precioInicial99 = p.getPrecioProducto();
				double precioRebajado99 = (p.getPrecioProducto() * 99) / 100;
				row[2] = String.format("%.2f", (precioInicial99 - precioRebajado99));
				break;
			}
			row[3] = p.getUnidadesProducto();
			for (Categoria c : categorias) {
				c = categoriaDAO.selectCategoriaById(p.getCategoria().getIdCategoria());
				row[4] = c.getNombreCategoria();
			}
			modelProducto.addRow(row);
		}
	}

	private static void updateUI(JFrame frame) {
		SwingUtilities.updateComponentTreeUI(frame);
		frame.validate();
		frame.repaint();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/arc-theme-orange.theme.json"));
					App window = new App();
					window.frmSupermercado.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		Pattern patDecimal = Pattern.compile("^\\d+(\\.\\d+)?$");
		Pattern patEntero = Pattern.compile("^\\d{1,4}$");

		Categoria categoria = new Categoria();
		Producto producto = new Producto();

		ProductoDAO productoDAO = new ProductoDAO();
		CategoriaDAO categoriaDAO = new CategoriaDAO();

		List<Categoria> categorias = null;
		List<Producto> productos = null;

		frmSupermercado = new JFrame();
		frmSupermercado.setBounds(100, 100, 1000, 625);
		frmSupermercado.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSupermercado.getContentPane().setLayout(null);

		menuBar = new JMenuBar();
		frmSupermercado.setJMenuBar(menuBar);
		
		JMenu mnApariencia = new JMenu("Apariencia");
		menuBar.add(mnApariencia);

		JMenuItem mntmOpcionFP = new JMenuItem("FirePunch");
		mntmOpcionFP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/arc-theme-orange.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a FirePunch");
			}
		});
		mnApariencia.add(mntmOpcionFP);

		JMenuItem mntmOpcionEM = new JMenuItem("Esmeralda");
		mntmOpcionEM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Gradianto_Nature_Green.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Esmeralda");
			}
		});
		mnApariencia.add(mntmOpcionEM);

		JMenuItem mntmOpcionAZ = new JMenuItem("Azerbaiyán");
		mntmOpcionAZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/Github Contrast.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Azerbaiyán");
			}
		});
		mnApariencia.add(mntmOpcionAZ);

		JMenuItem mntmOpcionMM = new JMenuItem("Mismagius");
		mntmOpcionMM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Material Palenight.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Mismagius");
			}
		});
		mnApariencia.add(mntmOpcionMM);

		JMenuItem mntmOpcionLM = new JMenuItem("Lemon Milk");
		mntmOpcionLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/oscuros/Monokai Pro Contrast.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Lemon Milk");
			}
		});
		mnApariencia.add(mntmOpcionLM);

		JMenuItem mntmOpcionAM = new JMenuItem("Azumarill");
		mntmOpcionAM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Moonlight.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Azumarill");
			}
		});
		mnApariencia.add(mntmOpcionAM);

		JMenuItem mntmOpcionFR = new JMenuItem("Frambuesa");
		mntmOpcionFR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Solarized Light.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(null, "Aspecto cambiado a Frambuesa");
			}
		});
		mnApariencia.add(mntmOpcionFR);

		JMenuItem mntmOpcionN2 = new JMenuItem("Naranjito 2");
		mntmOpcionN2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/oscuros/arc_theme_dark_orange.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Naranjito 2");
			}
		});
		mnApariencia.add(mntmOpcionN2);
		
		lblHomeSupermercado = new JLabel("Almacén del supermercado");
		lblHomeSupermercado.setFont(new Font("Dialog", Font.BOLD, 15));
		lblHomeSupermercado.setHorizontalAlignment(SwingConstants.CENTER);
		lblHomeSupermercado.setBounds(380, 28, 223, 15);
		frmSupermercado.getContentPane().add(lblHomeSupermercado);

		DefaultTableModel modelProducto = new DefaultTableModel() {
			public boolean isCellEditable(int fila, int columna) {
				return false; // No permitir la edición de las celdas
			}
		};

		modelProducto.addColumn("ID");
		modelProducto.addColumn("Nombre");
		modelProducto.addColumn("Precio");
		modelProducto.addColumn("Unidades");
		modelProducto.addColumn("Categoria");

		// productos = productoDAO.selectProductosByIdCategoria(1);
		productos = productoDAO.selectAllProducto();
		categorias = categoriaDAO.selectAllCategoria();

		for (Producto p : productos) {
			Object[] row = new Object[5];

			row[0] = p.getIdProducto();
			row[1] = p.getNombreProducto();
			row[2] = p.getPrecioProducto();
			row[3] = p.getUnidadesProducto();
			for (Categoria c : categorias) {
				c = categoriaDAO.selectCategoriaById(p.getCategoria().getIdCategoria());
				row[4] = c.getNombreCategoria();
			}
			modelProducto.addRow(row);
		}

		tableProducto = new JTable(modelProducto);
		tableProducto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indiceCategoria = 0;
				int index = tableProducto.getSelectedRow();
				TableModel model = tableProducto.getModel();

				tfIdProducto.setText(model.getValueAt(index, 0).toString());
				tfNombreProducto.setText(model.getValueAt(index, 1).toString());
				tfPrecioProducto.setText(model.getValueAt(index, 2).toString());
				tfUnidadesProducto.setText(model.getValueAt(index, 3).toString());

				switch (model.getValueAt(index, 4).toString()) {
				case "Frutas y Verduras":
					indiceCategoria = 0;
					break;
				case "Carnes y Aves":
					indiceCategoria = 1;
					break;
				case "Pescados y Mariscos":
					indiceCategoria = 2;
					break;
				case "Productos lácteos":
					indiceCategoria = 3;
					break;
				case "Panadería y Pastelería":
					indiceCategoria = 4;
					break;
				case "Bebidas":
					indiceCategoria = 5;
					break;
				case "Alimentos enlatados":
					indiceCategoria = 6;
					break;
				case "Cuidado personal":
					indiceCategoria = 7;
					break;
				case "Limpieza del hogar":
					indiceCategoria = 8;
					break;
				case "Higiene personal":
					indiceCategoria = 9;
					break;
				default:
					// indiceCategoria = 10;
				}
				cbCategoria.setSelectedIndex(indiceCategoria);
			}

		});
		tableProducto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel columnModel = tableProducto.getColumnModel();

		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);

		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setCellRenderer(centerRender);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(1).setCellRenderer(centerRender);
		columnModel.getColumn(2).setPreferredWidth(60);
		columnModel.getColumn(2).setCellRenderer(centerRender);
		columnModel.getColumn(3).setPreferredWidth(60);
		columnModel.getColumn(3).setCellRenderer(centerRender);
		columnModel.getColumn(4).setPreferredWidth(140);
		columnModel.getColumn(4).setCellRenderer(centerRender);

		frmSupermercado.getContentPane().add(tableProducto);
		tableProducto.setDefaultEditor(Producto.class, null);

		scrollPane = new JScrollPane(tableProducto);
		scrollPane.setBounds(162, 55, 660, 181);
		frmSupermercado.getContentPane().add(scrollPane);

		lblIdProducto = new JLabel("Id:");
		lblIdProducto.setBounds(332, 265, 70, 15);
		frmSupermercado.getContentPane().add(lblIdProducto);

		lblNombreProducto = new JLabel("Nombre:");
		lblNombreProducto.setBounds(332, 311, 70, 15);
		frmSupermercado.getContentPane().add(lblNombreProducto);

		lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(332, 352, 70, 15);
		frmSupermercado.getContentPane().add(lblPrecio);

		lblUnidades = new JLabel("Unidades:");
		lblUnidades.setBounds(332, 396, 96, 15);
		frmSupermercado.getContentPane().add(lblUnidades);

		lblCategoria = new JLabel("Categoría:");
		lblCategoria.setBounds(332, 441, 96, 15);
		frmSupermercado.getContentPane().add(lblCategoria);

		tfIdProducto = new JTextField();
		tfIdProducto.setEditable(false);
		tfIdProducto.setBounds(455, 263, 190, 20);
		frmSupermercado.getContentPane().add(tfIdProducto);
		tfIdProducto.setColumns(10);

		tfNombreProducto = new JTextField();
		tfNombreProducto.setColumns(10);
		tfNombreProducto.setBounds(455, 309, 190, 20);
		frmSupermercado.getContentPane().add(tfNombreProducto);

		tfPrecioProducto = new JTextField();
		tfPrecioProducto.setColumns(10);
		tfPrecioProducto.setBounds(455, 350, 190, 20);
		frmSupermercado.getContentPane().add(tfPrecioProducto);

		tfUnidadesProducto = new JTextField();
		tfUnidadesProducto.setColumns(10);
		tfUnidadesProducto.setBounds(455, 394, 190, 20);
		frmSupermercado.getContentPane().add(tfUnidadesProducto);

		cbOfertas = new JComboBox();
		cbOfertas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				refrescarTabla(modelProducto, productoDAO, categoriaDAO);
			}
		});
		cbOfertas.setModel(new DefaultComboBoxModel(new String[] { "Sin oferta", "Oferta del 25%", "Oferta del 50%",
				"Oferta del 75%", "Oferta de liquidación" }));
		cbOfertas.setBounds(671, 25, 151, 24);
		frmSupermercado.getContentPane().add(cbOfertas);

		cbCategoria = new JComboBox();
		cbCategoria.setBounds(455, 436, 190, 24);

		cbCategoriaFiltro = new JComboBox();
		cbCategoriaFiltro.setModel(new DefaultComboBoxModel(new String[] { "Ver todos", "Sin stock" }));
		cbCategoriaFiltro.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				refrescarTabla(modelProducto, productoDAO, categoriaDAO);
			}
		});
		cbCategoriaFiltro.setBounds(162, 25, 151, 24);

		categorias = categoriaDAO.selectAllCategoria();
		cbCategoria.removeAllItems();

		int numeral = 1;
		for (Categoria c : categorias) {
			cbCategoria.addItem(numeral + ". " + c.getNombreCategoria());
			cbCategoriaFiltro.addItem(numeral + ". " + c.getNombreCategoria());
			numeral++;
		}

		frmSupermercado.getContentPane().add(cbCategoriaFiltro);
		frmSupermercado.getContentPane().add(cbCategoria);

		btnInsertarProducto = new JButton("Insertar");
		btnInsertarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Matcher matPrecio = patDecimal.matcher(tfPrecioProducto.getText());
				Matcher matUnidades = patEntero.matcher(tfUnidadesProducto.getText());

				if (tfNombreProducto.getText().isEmpty() || tfPrecioProducto.getText().isEmpty()
						|| tfUnidadesProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Rellena todos los campos, por favor.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!matPrecio.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato del precio no es el correcto.\nEj: 27.56", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!matUnidades.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato de las unidades no es el correcto.\nEj: 89", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (Integer.parseInt(tfUnidadesProducto.getText()) < 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "Mínimo tiene que haber 0 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (Integer.parseInt(tfUnidadesProducto.getText()) > 9999) {
					JOptionPane.showMessageDialog(frmSupermercado, "No puede haber más de 9999 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					Categoria categoria = categoriaDAO.selectCategoriaById(cbCategoria.getSelectedIndex() + 1);
					Producto producto = new Producto(tfNombreProducto.getText(),
							Double.parseDouble(tfPrecioProducto.getText()),
							Integer.parseInt(tfUnidadesProducto.getText()), categoria);
					productoDAO.insertProducto(producto);
					JOptionPane.showMessageDialog(frmSupermercado, "Producto creado");
					refrescarTabla(modelProducto, productoDAO, categoriaDAO);
				}
			}
		});
		btnInsertarProducto.setBounds(285, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnInsertarProducto);

		btnActualizarProducto = new JButton("Actualizar");
		btnActualizarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Matcher matPrecio = patDecimal.matcher(tfPrecioProducto.getText());
				Matcher matUnidades = patEntero.matcher(tfUnidadesProducto.getText());

				if (tfNombreProducto.getText().isEmpty() || tfPrecioProducto.getText().isEmpty()
						|| tfUnidadesProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Rellena todos los campos, por favor.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!matPrecio.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato del precio no es el correcto.\nEj: 27.56", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!matUnidades.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato de las unidades no es el correcto.\nEj: 89", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (Integer.parseInt(tfUnidadesProducto.getText()) < 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "Mínimo tiene que haber 0 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (Integer.parseInt(tfUnidadesProducto.getText()) > 9999) {
					JOptionPane.showMessageDialog(frmSupermercado, "No puede haber más de 9999 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Categoria categoria = categoriaDAO.selectCategoriaById(cbCategoria.getSelectedIndex() + 1);
					Producto producto = productoDAO.selectProductoById(Integer.parseInt(tfIdProducto.getText()));
					producto.setNombreProducto(tfNombreProducto.getText());
					producto.setPrecioProducto(Double.parseDouble(tfPrecioProducto.getText()));
					producto.setUnidadesProducto(Integer.parseInt(tfUnidadesProducto.getText()));
					producto.setCategoria(categoria);
					productoDAO.updateProducto(producto);
					JOptionPane.showMessageDialog(frmSupermercado, "Producto actualizado");
					refrescarTabla(modelProducto, productoDAO, categoriaDAO);
				}
			}
		});
		btnActualizarProducto.setBounds(441, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnActualizarProducto);

		btnEliminarProducto = new JButton("Eliminar");
		btnEliminarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tfIdProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Selecciona un producto a borrar, por favor.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
				productoDAO.deleteProducto(Integer.parseInt(tfIdProducto.getText()));
				JOptionPane.showMessageDialog(frmSupermercado, "Producto borrado");
				refrescarTabla(modelProducto, productoDAO, categoriaDAO);
				}
			}
		});
		btnEliminarProducto.setBounds(595, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnEliminarProducto);

	}
}
