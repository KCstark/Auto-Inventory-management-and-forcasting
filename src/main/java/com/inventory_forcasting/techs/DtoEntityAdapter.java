package com.inventory_forcasting.techs;


public interface DtoEntityAdapter<D,E> {

    public  E convertToEntity(D dto);
    public  D convertToDto(E entity);
}
