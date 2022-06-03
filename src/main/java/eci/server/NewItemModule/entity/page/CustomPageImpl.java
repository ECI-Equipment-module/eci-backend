package eci.server.NewItemModule.entity.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class CustomPageImpl<T> extends PageImpl<T> {
    private final List<String> indexes;
    //새로운 인스턴스 변수 생성


    public CustomPageImpl(List<T> content, Pageable pageable, long total, List<String> indexes) {
        super(content, pageable, total);
        this.indexes = indexes;
    }

    public CustomPageImpl(List<T> content, List<String> indexes) {
        super(content);
        this.indexes = indexes;
    }

    @JsonGetter(value = "contents")
    @Override
    public List getContent() {
        return super.getContent();
    }

    @JsonGetter(value = "indexes")
    public List getPaging() {
        return indexes;
    }

}