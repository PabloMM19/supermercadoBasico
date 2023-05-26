package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.model.Categoria;
import com.hibernate.util.HibernateUtil;

public class CategoriaDAO {
	public void insertCategoria(Categoria c) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(c);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	public void updateCategoria(Categoria c) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(c);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	public void deleteCategoria(int codigo) {

		Transaction transaction = null;
		Categoria c = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			c = session.get(Categoria.class, codigo);
			session.remove(c);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}
	
	public List<Categoria> selectAllCategoria() {

		Transaction transaction = null;
		List<Categoria> categorias = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			categorias = session.createQuery("from Categoria", Categoria.class).getResultList();
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return categorias;
	}
	
	public Categoria selectCategoriaById(int codigo) {

		Transaction transaction = null;
		Categoria c = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			c = session.get(Categoria.class, codigo);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return c;
	}
}
