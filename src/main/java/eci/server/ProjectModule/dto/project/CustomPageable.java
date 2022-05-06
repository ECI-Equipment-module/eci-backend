package eci.server.ProjectModule.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageable implements Pageable {
    @Override
    public int getNumberOfPages() {
        return 0;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

    @Null
    private Long memberId;
}
