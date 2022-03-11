package com.sano.schoolmanApi.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sano.schoolmanApi.base.BaseRepository;
import com.sano.schoolmanApi.pagination.DataTableRequest;
import com.sano.schoolmanApi.pagination.DataTableResults;
import com.sano.schoolmanApi.pagination.PaginationCriteria;
import com.sano.schoolmanApi.util.Response;

/**
 * @author Md. Sanowar Ali
 *
 */

@Repository
@Transactional
public class StudentRepository extends BaseRepository {

	// Grid List
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response gridList(HttpServletRequest request) {
		Response response = new Response();
		DataTableResults<StudentEntity> dataTableResults = null;

		StudentEntity studentEntity = new StudentEntity();
		DataTableRequest dataTableInRQ = new DataTableRequest(request);
		List gridList = new ArrayList<>();

		// String studentNo = request.getParameter("studentNo");
		// if (studentNo != null) {
		// infertilityLookupDtlEntity.setId(Long.parseLong(studentNo));
		// }

		Long totalRowCount = totalCount(studentEntity);
		response = baseList(typedQuery(studentEntity, dataTableInRQ));
		if (response.isSuccess()) {
			if (response.getItems() != null) {
				gridList = response.getItems();
			}
			dataTableResults = dataTableResults(dataTableInRQ, countTypedQuery(studentEntity, dataTableInRQ), gridList,
					totalRowCount);
		}
		response.setItems(null);
		response.setObj(dataTableResults);
		return response;
	}

	// Get All List
	public Response getAllList(String reqObj) {
		StudentEntity studentEntity = null;
		if (null != reqObj) {
			studentEntity = objectMapperReadValue(reqObj, StudentEntity.class);
		}
		return baseList(criteriaQuery(studentEntity));
	}

	// findById or any other response type
	public Response findById(Long id) {
		Response response = new Response();
		StudentEntity studentEntity = new StudentEntity();

		studentEntity.setId(id);
		response = baseList(criteriaQuery(studentEntity));
		if (response.isSuccess()) {
			return response;
		}
		return getErrorResponse("Record not Found !!");
	}

	// FindById Entity Type
	public StudentEntity findId(Long id) {
		StudentEntity studentEntity = new StudentEntity();

		studentEntity.setId(id);
		Response response = baseFindById(criteriaQuery(studentEntity));
		if (response.isSuccess()) {
			return getValueFromObject(response.getObj(), StudentEntity.class);
		}
		return null;
	}

	// Save
	public Response save(String reqObj) {
		Response response = new Response();
		StudentEntity studentObj = objectMapperReadValue(reqObj, StudentEntity.class);
		if (studentObj == null) {
			return getErrorResponse("Data not found for Save!");
		}
		// Long companyNo = 1L;
		// studentObj.setId(generatestudentNo(companyNo));
		response = baseOnlySave(studentObj);
		if (response.isSuccess()) {
			response.setMessage("Successsfully Data Saved!");
			return response;
		}
		return getErrorResponse("Data Not Saved!");
	}

	// Update
	public Response update(String reqObj) {
		Response response = new Response();
		StudentEntity studentObj = objectMapperReadValue(reqObj, StudentEntity.class);
		StudentEntity obj = findId(studentObj.getId());
		if (obj != null) {
			response = baseUpdate(studentObj);
			if (response.isSuccess()) {
				response.setMessage("Successsfully Data Updated!");
				return response;
			}
		}
		return getErrorResponse("Data not Found for Update!");
	}

	// Delete
	public Response delete(Long id) {
		StudentEntity studentObj = findId(id);
		if (studentObj == null) {
			return getErrorResponse("Record not found!");
		}
		return baseDelete(studentObj);
	}

	private Long totalCount(StudentEntity filter) {
		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<StudentEntity> root = from(StudentEntity.class, criteriaQuery);
		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root));
	}

	@SuppressWarnings({ "unchecked" })
	private List<Predicate> criteriaCondition(StudentEntity filter, CriteriaBuilder builder, Root<StudentEntity> root) {
		if (builder == null) {
			builder = super.builder;
		}
		if (root == null) {
			root = super.root;
		}
		List<Predicate> p = new ArrayList<Predicate>();
		if (filter != null) {
			if (filter.getId() != null && filter.getId() > 0) {
				Predicate condition = builder.equal(root.get("id"), filter.getId());
				p.add(condition);
			}
		}
		return p;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private void init() {
		initEntityManagerBuilderCriteriaQueryRoot(StudentEntity.class);
		CriteriaBuilder builder = super.builder;
		CriteriaQuery criteria = super.criteria;
		Root root = super.root;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CriteriaQuery criteriaQuery(StudentEntity filter) {
		init();
		List<Predicate> p = new ArrayList<Predicate>();
		p = criteriaCondition(filter, null, null);

		if (!CollectionUtils.isEmpty(p)) {
			Predicate[] pArray = p.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}
		return criteria;
	}

	@SuppressWarnings({ "rawtypes" })
	private <T> TypedQuery typedQuery(StudentEntity filter, DataTableRequest<T> dataTableInRQ) {
		init();
		List<Predicate> pArrayJoin = new ArrayList<Predicate>();
		List<Predicate> pConjunction = criteriaCondition(filter, null, null);
		List<Predicate> pDisJunction = dataTablefilter(dataTableInRQ);
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
		if (dataTableInRQ.getOrder().getName() != null && !dataTableInRQ.getOrder().getName().isEmpty()) {
			if (dataTableInRQ.getOrder().getSortDir().equals("ASC")) {
				criteria.orderBy(builder.asc(root.get(dataTableInRQ.getOrder().getName())));
			} else {
				criteria.orderBy(builder.desc(root.get(dataTableInRQ.getOrder().getName())));
			}

		}
		criteria.where(pArrayJoin.toArray(new Predicate[0]));
		return baseTypedQuery(criteria, dataTableInRQ);
	}

	private <T> Long countTypedQuery(StudentEntity filter, DataTableRequest<T> dataTableInRQ) {

		if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
			return 0l;
		}

		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<StudentEntity> root = from(StudentEntity.class, criteriaQuery);
		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root),
				dataTablefilter(dataTableInRQ, builder, root));
	}

	@Override
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

}
