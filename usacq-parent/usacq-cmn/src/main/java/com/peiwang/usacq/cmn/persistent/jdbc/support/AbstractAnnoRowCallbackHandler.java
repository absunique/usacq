package com.peiwang.usacq.cmn.persistent.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

abstract public class AbstractAnnoRowCallbackHandler<T> implements RowCallbackHandler {

    private Class<T>            elementType;

    public AbstractAnnoRowCallbackHandler(Class<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        T entity = new AnnotationRowMapper<T>(elementType).mapRow(rs, 1);
        processEntity(entity);
    }

    protected abstract void processEntity(T entity);
}
