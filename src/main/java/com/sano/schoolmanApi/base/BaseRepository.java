package com.sano.schoolmanApi.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.QueryHints;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import com.sano.schoolmanApi.pagination.DataTableRequest;
import com.sano.schoolmanApi.pagination.PaginationCriteria;
import com.sano.schoolmanApi.util.CommonFunctions;
import com.sano.schoolmanApi.util.Response;

public class BaseRepository implements CommonFunctions {

	private final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

	@PersistenceContext
	private EntityManager entityManager;

	public CriteriaBuilder builder = null;
	public CriteriaQuery criteria = null;
	public Root root = null;

	@Autowired
	private Environment env;

	public Response baseOnlySave(Object obj) {
		Response response = new Response();
		try {
			entityManager.persist(obj);
			response.setObj(obj);
			return getSuccessResponse("Saved Successfully", response);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return getErrorResponse("Save fail !!");
		}

	}

	public Response baseUpdate(Object obj) {

		Response response = new Response();
		try {

			response.setObj(entityManager.merge(obj));

			return getSuccessResponse("Updated Successfully", response);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return getErrorResponse("Update fail !!");
		}

	}

	public Response baseRemove(Object obj) {

		try {
			entityManager.merge(obj);
			return getSuccessResponse("Remove Successfully");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Remove fail !!");
		}

	}

	public Response baseDelete(Object obj) {
		try {
			entityManager.remove(obj);
			return getSuccessResponse("Delete Successfully");
		} catch (Exception e) {
			return getErrorResponse("Delete fail !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseList(CriteriaQuery criteria) {
		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {
				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes" })
	public Response baseList(TypedQuery typedQuery) {
		Response response = new Response();
		List list = null;

		try {

			list = typedQuery.setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {

				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");

		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseFindById(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {
			obj = entityManager.createQuery(criteria).getSingleResult();
			response.setObj(obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		}

	}

	@SuppressWarnings({ "rawtypes" })
	public <T> TypedQuery typedQuery(List<Predicate> pConjunctionParam, List<Predicate> pDisJunctionParam) {

		List<Predicate> pArrayJoin = new ArrayList<Predicate>();

		List<Predicate> pConjunction = pConjunctionParam;
		List<Predicate> pDisJunction = pDisJunctionParam;

		Predicate predicateAND = null;
		Predicate predicateOR = null;

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			predicateAND = builder.and(pArray);
		}
		if (!CollectionUtils.isEmpty(pDisJunction)) {
			Predicate[] pArray = pDisJunction.toArray(new Predicate[] {});
			predicateOR = builder.or(pArray);
		}
		if (predicateAND != null) {
			pArrayJoin.add(predicateAND);
		}
		if (predicateOR != null) {
			pArrayJoin.add(predicateOR);
		}
		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		return baseTypedQuery(criteria);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> TypedQuery baseTypedQuery(CriteriaQuery criteria, DataTableRequest dataTableInRQ) {

		CriteriaQuery<T> select = criteria.select(root);

		TypedQuery<T> typedQuery = entityManager.createQuery(select);
		typedQuery.setFirstResult(dataTableInRQ.getStart());
		typedQuery.setMaxResults(dataTableInRQ.getLength());

		return typedQuery;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> TypedQuery baseTypedQuery(CriteriaQuery criteria, int start, int length) {

		CriteriaQuery<T> select = criteria.select(root);

		TypedQuery<T> typedQuery = entityManager.createQuery(select);
		typedQuery.setFirstResult(start);
		typedQuery.setMaxResults(length);

		return typedQuery;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> TypedQuery baseTypedQuery(CriteriaQuery criteria) {

		CriteriaQuery<T> select = criteria.select(root);
		TypedQuery<T> typedQuery = entityManager.createQuery(select);

		return typedQuery;
	}

	public <T> Long totalCount(Class<T> clazz, List<Predicate> p) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		criteria.select(builder.count(criteria.from(clazz)));

		/*
		 * if (!CollectionUtils.isEmpty(p)) {
		 *
		 * Predicate[] pArray = p.toArray(new Predicate[] {}); Predicate predicate =
		 * builder.and(pArray); criteria.where(predicate); }
		 */

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public <T> Long totalCount(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root<T> root,
			List<Predicate> pConjunction) {

		criteria.select(builder.count(root));

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public <T> Long totalCount(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root<T> root,
			List<Predicate> pConjunction, List<Predicate> pDisjunction) {

		List<Predicate> pArrayJoin = new ArrayList<Predicate>();
		Predicate predicateAND = null;
		Predicate predicateOR = null;

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			predicateAND = builder.and(pArray);
		}
		if (!CollectionUtils.isEmpty(pDisjunction)) {
			Predicate[] pArray = pDisjunction.toArray(new Predicate[] {});
			predicateOR = builder.or(pArray);
		}
		if (predicateAND != null) {
			pArrayJoin.add(predicateAND);
		}
		if (predicateOR != null) {
			pArrayJoin.add(predicateOR);
		}

		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		criteria.select(builder.count(root));

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}
	
	@SuppressWarnings("unchecked")
	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();
				if (!pair.getKey().equals("ssModifiedOn")) {
					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							pair.getValue().toLowerCase()));
				}

			}

		}

		return p;

	}

	@SuppressWarnings("unchecked")
	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ, Class clazz) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();
			Field[] fields = getClassFields(clazz);

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();

				String dataType = getClassFieldDataType(fields, pair.getKey());

				if (dataType != null && dataType.equals("class java.lang.String")) {
					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							pair.getValue().toLowerCase()));
				}

			}

		}

		return p;

	}

	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ, CriteriaBuilder builder, Root root) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();
				if (!pair.getKey().equals("ssModifiedOn")) {

					// System.out.println("pair.getKey() " + pair.getKey());

					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							pair.getValue().toLowerCase()));
				}

			}

		}

		return p;

	}

	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ, CriteriaBuilder builder, Root root,
			Class clazz) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();
			Field[] fields = getClassFields(clazz);

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();

				String dataType = getClassFieldDataType(fields, pair.getKey());

				if (dataType != null && dataType.equals("class java.lang.String")) {
					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							pair.getValue().toLowerCase()));
				}

			}

		}

		return p;

	}

	public CriteriaQuery<Long> longCriteriaQuery(CriteriaBuilder builder) {
		return builder.createQuery(Long.class);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void initEntityManagerBuilderCriteriaQueryRoot(Class clazz) {
		criteriaRoot(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Root criteriaRoot(Class clazz) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(clazz);
		Root root = criteria.from(clazz);
		this.builder = builder;
		this.criteria = criteria;
		this.root = root;

		return root;
	}

	public CriteriaBuilder criteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}
	
	public Field[] getClassFields(Class claz) {
		return claz.getDeclaredFields();
	}

	public String getClassFieldDataType(Field[] fields, String fieldName) {

		String fieldType = "";

		for (Field field : fields) {

			if (field.getName().equals(fieldName)) {

				return fieldType = field.getType().toString();
			}

		}

		return null;

	}
	
	public <T> Root<T> from(Class<T> clazz, CriteriaQuery<Long> criteria) {
		return criteria.from(clazz);
	}

	/**
	 * @param companyNo
	 * @param tableName
	 * @param columnName
	 * @param dataLength
	 * @return
	 */
	public Long functionFdAutoNo(Long companyNo, String tableName, String columnName, Long dataLength) {
		BigDecimal maxValue = null;
		maxValue = (BigDecimal) entityManager
				.createNativeQuery("SELECT FD_AUTO_NO(:pTable,:pColumn,:pCompanyNo,:pDataLength) FROM DUAL")
				.setParameter("pTable", tableName).setParameter("pColumn", columnName)
				.setParameter("pCompanyNo", companyNo).setParameter("pDataLength", dataLength).getSingleResult();

		if (maxValue == null) {
			return null;
		}
		return maxValue.longValue();

	}

	/**
	 * @param companyNo
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public Long functionFdAutoNo(Long companyNo, String tableName, String columnName) {
		return functionFdAutoNo(companyNo, tableName, columnName, 6L);

	}

	public Connection getOraConnection() {

		try {

			Class.forName(env.getProperty("spring.datasource.driver-class-name"));

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");

		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
		}
		if (connection != null) {
			return connection;
		} else {
			System.out.println("Failed to make connection!");
			return null;
		}

	}

	public Long generateTodoNo(Long no) {
		return functionFdAutoNo(no, "to_do", "todo_no", 9L);
	}
}
