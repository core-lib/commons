package com.qfox.commons.converter.test;

import java.io.Serializable;
import java.util.Date;

public interface Domain<T extends Serializable> {

	public T getId();

	public void setId(T id);

	public Date getDateCreated();

	public void setDateCreated(Date date);

}
