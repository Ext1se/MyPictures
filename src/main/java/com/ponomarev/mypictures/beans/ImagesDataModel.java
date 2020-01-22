package com.ponomarev.mypictures.beans;

import com.ponomarev.mypictures.models.Image;
import com.ponomarev.mypictures.repositories.ImageRepository;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class ImagesDataModel extends LazyDataModel<Image> {

    private List<Image> images;
    private Pager pager;
    private ImageRepository repository;

    public ImagesDataModel(Pager pager, ImageRepository repository) {
        this.pager = pager;
        this.repository = repository;
    }

    @Override
    public Image getRowData(String rowKey) {
        System.out.println("getRowData");
        for (Image image : images) {
            if (image.getId() == Long.valueOf(rowKey)) {
                return image;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Image image) {
        System.out.println("getRowKey");
        return image.getId();
    }

    @Override
    public List<Image> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        System.out.println("load 1");
        pager.setFrom(first);
        pager.setTo(pageSize);
        repository.updatePage();
        setRowCount(pager.getTotalCount());
        return pager.getImages();
    }

    @Override
    public List<Image> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        System.out.println("load 2");
        return super.load(first, pageSize, multiSortMeta, filters); 
    }
}
