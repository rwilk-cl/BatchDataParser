package com.corelogic.batch.batch;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.corelogic.batch.model.Backlog;
import com.google.common.io.Resources;

/**
 * Custom ItemReader reads xlsx or xls file, convert rows to Backlog object and return it.
 */
public class CustomBacklogReader implements ResourceAwareItemReaderItemStream {

  private int nextBacklogIndex;
  private Resource resource;
  private int linesToSkip;

  public CustomBacklogReader() {
    nextBacklogIndex = 0;
  }

  @Override
  public Backlog read() throws Exception {

    Backlog backlog = new Backlog();
    Workbook workbook = WorkbookFactory.create(
        new File(Resources.getResource(convertPath((((FileSystemResource) resource).getPath()))).getFile()));
    //WorkbookFactory.create(new ClassPathResource(((ServletContextResource) resource).getPath()).getFile()); //(new File(path));

    for (Sheet sheet : workbook) {
      DataFormatter dataFormatter = new DataFormatter();
      Iterator<Row> rowIterator = sheet.rowIterator();

      int rowIndex = 0;
      while (rowIterator.hasNext()) {

        Row row = rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();

        if (rowIndex == nextBacklogIndex) {

          while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
              case 0:
                backlog.setSummary(dataFormatter.formatCellValue(cell));
                break;
              case 1:
                backlog.setEpic(dataFormatter.formatCellValue(cell));
                break;
              case 2:
                backlog.setBacklog(dataFormatter.formatCellValue(cell));
                break;
              case 3:
                backlog.setSubTask(dataFormatter.formatCellValue(cell));
                break;
              case 4:
                backlog.setStatus(dataFormatter.formatCellValue(cell));
                break;
            }
          }
          nextBacklogIndex++;
          workbook.close();
          return backlog;
        } else {
          rowIndex++;
        }
      }
    }
    workbook.close();
    return null;
  }

  public void setLinesToSkip(int linesToSkip) {
    this.linesToSkip = linesToSkip;
    this.nextBacklogIndex = linesToSkip;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {

  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {

  }

  @Override
  public void close() throws ItemStreamException {

  }

  @Override
  public void setResource(Resource resource) {
    this.resource = resource;
    nextBacklogIndex = linesToSkip;
  }

  private String convertPath(String path) {
    int lastIndex = path.lastIndexOf("resources");
    return ((FileSystemResource) resource).getPath().substring(lastIndex + 10);
  }
}
