package org.envtools.monitor.module.querylibrary.services.export;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IAvdeev on 18.01.2017.
 */
@Service
public class ZipArchiveExportService {

    public static final String ZIP_SEPARATOR = "/";

    @Autowired
    CategoryDao categoryDao;

    public void writeArchive(OutputStream outputStream) throws IOException
    {
        List<Category> categories = categoryDao.getRootCategories();

        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {

            for (Category rootCategory : categories) {
                appendCategory(zos, rootCategory, "");
            }
        }
    }

    private void appendCategory(ZipOutputStream zos, Category cat, String prefixPath) throws IOException {

        prefixPath = prefixPath + ZipArchiveFilenameConverter.getDirnameFromCategory(cat) + ZIP_SEPARATOR;

        for (LibQuery query : cat.getQueries()) {
            addQueryFile(zos, query, prefixPath);
        }
        for (Category childCategory : cat.getChildCategories()) {
            appendCategory(zos, childCategory, prefixPath);
        }
    }

    private void addQueryFile(ZipOutputStream zos, LibQuery query, String prefixPath) throws IOException {
        String filenameFromTitle = ZipArchiveFilenameConverter.getFilenameFromQuery(query);
        ZipEntry entry = new ZipEntry(prefixPath + filenameFromTitle);
        zos.putNextEntry(entry);
        zos.write(generateSqlQueryFileContents(query).getBytes());
        zos.closeEntry();
    }

    private String generateSqlQueryFileContents(LibQuery query) throws IOException
    {
        String result = "";

        result += String.format("--TITLE: %s%n", query.getTitle());
        if (query.getDescription() != null) {
            result += String.format("--DESCRIPTION: %s", query.getDescription());
        }

        for (QueryParam param : query.getQueryParams()) {
            result += String.format("--PARAM: %s:%s%n", param.getName(), param.getType());
        }

        result += String.format("%n%s", query.getText());
        return result;
    }
}
