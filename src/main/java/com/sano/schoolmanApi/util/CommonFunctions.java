package com.sano.schoolmanApi.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sano.schoolmanApi.pagination.DataTableRequest;
import com.sano.schoolmanApi.pagination.DataTableResults;

/**
 * Common Functions Interface
 */

public interface CommonFunctions {

	default Response getSuccessResponse(String message) {
		Response response = new Response();
		response.setSuccess(true);
		response.setMessage(message);
		return response;
	}

	default Response getSuccessResponse(String message, Response response) {
		response.setSuccess(true);
		response.setMessage(message);
		return response;
	}

	default Response getErrorResponse(String message) {
		Response response = new Response();
		response.setSuccess(false);
		response.setMessage(message);
		return response;
	}

	default Response getErrorResponse(String message, Response response) {
		response.setSuccess(false);
		response.setMessage(message);
		return response;
	}

	default <T> T objectMapperReadValue(String content, Class<T> valueType) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			return objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, valueType);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// return null;
	}
	
	default <T> DataTableResults<T> dataTableResults(DataTableRequest<T> dtr,
			Long pFilterCount, List<T> pList, Long totalRecord) {

		DataTableResults<T> dataTableResult = new DataTableResults<T>();
		dataTableResult.setDraw(dtr.getDraw());

		if (dtr.isGlobalSearch()) {
			dataTableResult.setData(pList);
		} else {
			dataTableResult.setData(pList);
		}

		if ((pList != null && pList.size() > 0)) {

			dataTableResult.setRecordsTotal(String.valueOf(totalRecord));

			if (dtr.getPaginationRequest().isFilterByEmpty()) {
				dataTableResult.setRecordsFiltered(String.valueOf(totalRecord));

			} else {
				dataTableResult.setRecordsFiltered(Long.toString(pFilterCount));
			}

		} else {
			dataTableResult.setRecordsTotal("0");
			dataTableResult.setRecordsFiltered("0");
		}

		return dataTableResult;

	}

	@SuppressWarnings("unchecked")
	default <T> T getValueFromObject(Object data, Class<T> clazz) {
		if (data == null) {
			return null;
		}
		return (T) data;
	}
}
