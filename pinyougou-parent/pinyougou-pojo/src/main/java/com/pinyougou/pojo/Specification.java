package com.pinyougou.pojo;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
    private TbSpecification specification;//一个规格
    private List<TbSpecificationOption> optionList;//多个规格选项

    public Specification() {
    }

    public Specification(TbSpecification specification, List<TbSpecificationOption> optionList) {
        this.specification = specification;
        this.optionList = optionList;
    }

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<TbSpecificationOption> optionList) {
        this.optionList = optionList;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "specification=" + specification +
                ", optionList=" + optionList +
                '}';
    }
}
