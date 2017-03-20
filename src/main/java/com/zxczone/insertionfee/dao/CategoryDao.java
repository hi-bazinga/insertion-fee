package com.zxczone.insertionfee.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.zxczone.insertionfee.pojo.Category;

/**
 * @author Jason Zhao
 * @date Mar 18, 2017
 */
@Repository
public class CategoryDao {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryDao.class);

    private static final LinkedHashMap<Integer, Category> categories = new LinkedHashMap<>();

    private static Category root;

    @Value("${category.data.file}")
    private String dataFilePath;
    
    @PostConstruct
    public void init() {
        loadFromTreeText(new File(dataFilePath));
    }

    /**
     * Load categories data from file
     */
    public void loadFromEntryText(File file) {
        LOG.info("Loading categories data from row text...");
        categories.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Category cat = createCategoryFromStr(line);     // First line is column names
                if (cat != null) {
                    categories.put(cat.getId(), cat);
                }
            }
            root = buildCategoryTree();
            LOG.info("Categories data loaded successfully!");
        } catch (IOException e) {
            LOG.error("Failed to load data file!", e);
        } catch (RuntimeException re) {
            LOG.error("Malformed data format!", re);
        }
    }
    
    /**
     * Load data from tree text. 
     * @param file
     */
    public void loadFromTreeText(File file) {
        LOG.info("Loading categories data from tree text...");
        categories.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Stack<Category> stack = new Stack<>();
            Stack<Integer> indentStack = new Stack<>();
            
            int curIndent = 0, index = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                curIndent = line.split("-")[0].length();
                
                Category cat = parse(line);
                cat.setId(index);
                cat.setIndent(curIndent);
                categories.put(index, cat);
                
                if (indentStack.isEmpty()) {
                    root = cat;
                    stack.push(cat);
                    indentStack.push(curIndent);
                } else if (indentStack.peek() < curIndent) {
                    // is child
                    stack.peek().setFirstChildId(index);
                    cat.setParentId(index - 1);
                    stack.push(cat);
                    indentStack.push(curIndent);
                } else if (indentStack.peek() - curIndent == 0) {  // wrong: indentStack.peek() == curIndent
                    // is sibling
                    Category preCat = stack.pop();  // we only need to track one category in the same level, so pop previous one.
                    indentStack.pop();
                    
                    preCat.setNextSiblingId(index);
                    cat.setParentId(preCat.getParentId());
                    stack.push(cat);
                    indentStack.push(curIndent);
                } else {
                    // enter another parent level
                    while (indentStack.pop() - curIndent > 0) {
                        stack.pop();
                    }
                    Category preSibling = stack.pop();
                    preSibling.setNextSiblingId(index);
                    cat.setParentId(preSibling.getParentId());
                    stack.push(cat);
                    indentStack.push(curIndent);
                }
                index ++;
            }
            root = buildCategoryTree();
            LOG.info("Categories data loaded successfully!");
        } catch (IOException e) {
            LOG.error("Failed to load data file!", e);
        } catch (RuntimeException re) {
            LOG.error("Malformed data format!", re);
        }
    }

    public Category getCategoryTree() {
        System.out.println(root);
        return root;
    }
    
    public boolean isRoot(int catId) {
        return root.getId() == catId;
    }

    /**
     * Get category by ID
     * 
     * @param catId
     *            category ID
     * @return category object
     */
    public Category getCategoryById(int catId) {
        return categories.get(catId);
    }

    /**
     * Get parent category of the given category
     * 
     * @param catId
     *            category ID
     * @return the parent category object
     */
    public Category getParentOfCategory(int catId) {
        Category cat = getCategoryById(catId);
        return cat.getParentId() != null ? categories.get(cat.getParentId()) : null;
    }

    /**
     * Get children list of the given category
     * 
     * @param catId
     *            category ID
     * @return children list of this category
     */
    public List<Category> getChildrenOfCategory(int catId) {
        List<Category> children = new ArrayList<>();
        Category cat = getCategoryById(catId);
        if (cat.getFirstChildId() != null) {
            Category curCat = getCategoryById(cat.getFirstChildId());
            while (curCat != null) {
                children.add(curCat);
                curCat = getCategoryById(curCat.getNextSiblingId());
            }
        }
        return children;
    }

    /**
     * Build categories tree
     */
    private Category buildCategoryTree() {
        Queue<Category> queue = new LinkedList<>();

        Iterator<Category> catItr = categories.values().iterator();
        Category root = catItr.next();
        queue.add(root);

        while (!queue.isEmpty()) {
            Category parentCat = queue.remove();
            Integer childId = parentCat.getFirstChildId();

            // Loop to add children
            while (childId != null) {
                Category child = categories.get(childId);
                if (child != null) {
                    parentCat.getChildren().add(child);
                    // put category into a queue for populating its children
                    queue.add(child);
                }
                childId = child.getNextSiblingId();
            }
        }

        return root;
    }

    /**
     * Create category object from string, we assume that the data file should
     * be well organized.
     * 
     * @param str
     *            one line of text in data file
     * @return category object
     */
    private Category createCategoryFromStr(String str) {
        Category category = null;
        try {
            category = new Category();
            String[] fields = str.split(",");
            category.setId(Integer.parseInt(fields[0]));
            category.setName(fields[1]);
            category.setPrice(toInteger(fields[2]));
            category.setParentId(toInteger(fields[3]));
            category.setFirstChildId(toInteger(fields[4]));
            category.setNextSiblingId(toInteger(fields[5]));
        } catch (Exception e) {
            LOG.error("Failed to create categories from string: " + str);
        }
        return category;
    }
    
    private Category parse(String str) {
        // Get price
        Pattern pattern = Pattern.compile("\\-\\s*(.+?)\\s{2,}\\[(\\d+){0,1}\\]");
        Matcher m = pattern.matcher(str);
        m.find();
        String nameStr = m.group(1);
        String priceStr = m.group(2);
        
        Category cat = new Category();
        cat.setName(nameStr);
        cat.setPrice(priceStr != null ? Integer.parseInt(priceStr) : null);
        return cat;
    }

    /**
     * Only for null check, we use "null" as the placeholder of null.
     */
    private Integer toInteger(String str) {
        return "null".equals(str) ? null : Integer.parseInt(str);
    }

}
