package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.hibernate.model.Producto;
import com.hibernate.util.HibernateUtil;

public class ProductoDAO {
	public void insertProducto(Producto p) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	public void updateProducto(Producto p) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	public void deleteProducto(int codigo) {

		Transaction transaction = null;
		Producto p = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.get(Producto.class, codigo);
			session.remove(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}
	
	public List<Producto> selectAllProducto() {

		Transaction transaction = null;
		List<Producto> productos = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			productos = session.createQuery("from Producto", Producto.class).getResultList();
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productos;
	}
	
	public List<Producto> selectProductosByIdCategoria(int idCategoria) {

		Transaction transaction = null;
		List<Producto> productoDeCategoria = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Query<Producto> query=session.createQuery("FROM Producto WHERE categoria.idCategoria = :idCategoria",Producto.class);
			query.setParameter("idCategoria", idCategoria);
			productoDeCategoria=query.getResultList();

			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productoDeCategoria;
	}
	
	public List<Producto> selectProductosSinStock() {

		Transaction transaction = null;
		List<Producto> productoSinStock = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Query<Producto> query=session.createQuery("FROM Producto WHERE unidadesProducto = 0",Producto.class);
			productoSinStock=query.getResultList();

			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productoSinStock;
	}
	
	public Producto selectProductoById(int codigo) {

		Transaction transaction = null;
		Producto p = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.get(Producto.class, codigo);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return p;
	}
}
