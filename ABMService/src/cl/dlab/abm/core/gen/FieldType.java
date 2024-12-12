//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.17 at 04:44:18 PM CLT 
//


package cl.dlab.abm.core.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FieldType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="sqlName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="property" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldType")
public class FieldType implements Serializable 
{

    @XmlElement(name = "field", required = true)
    protected List<FieldType> field;
	
    @XmlAttribute(name = "sqlName")
    protected String sqlName;
    @XmlAttribute(name = "property")
    protected String property;
    @XmlAttribute(name = "type")
    protected Type type;
    @XmlAttribute(name = "key")
    protected Boolean key;
    @XmlAttribute(name = "akey")
    protected Boolean akey;
    
    @XmlAttribute(name = "dkey")
    protected Boolean dkey;
    
	@XmlAttribute(name = "autoincrement")
    protected Boolean autoincrement;
    @XmlAttribute(name = "sort")
    protected Integer sort;
    @XmlAttribute(name = "sortType")
    protected SortType sortType = SortType.Asc;

    @XmlAttribute(name = "typeRef")
    protected Type typeRef;
    @XmlAttribute(name = "sqlNameRef")
    protected String sqlNameRef;
    @XmlAttribute(name = "joinRef")
    protected String joinRef;
    @XmlAttribute(name = "operator")
    protected String operator;
    @XmlAttribute(name = "keyGenerator")
    protected Boolean keyGenerator;
    

	public List<FieldType> getField() {
        if (field == null) {
            field = new ArrayList<FieldType>();
        }
        return this.field;
    }
    
    /**
     * Gets the value of the sqlName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSqlName() {
        return sqlName;
    }

    /**
     * Sets the value of the sqlName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSqlName(String value) {
        this.sqlName = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the value of the property property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProperty(String value) {
        this.property = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

	/**
	 * @return the key
	 */
	public Boolean getKey()
	{
		return key == null ? Boolean.FALSE : key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Boolean key)
	{
		this.key = key;
	}

	/**
	 * @return the akey
	 */
	public Boolean getAkey()
	{
		return akey == null ? Boolean.FALSE : akey;
	}

	/**
	 * @param akey the akey to set
	 */
	public void setAkey(Boolean akey)
	{
		this.akey = akey;
	}

	/**
	 * @return the sqlNameRef
	 */
	public String getSqlNameRef()
	{
		return sqlNameRef;
	}

	/**
	 * @return the typeRef
	 */
	public Type getTypeRef()
	{
		return typeRef;
	}

	/**
	 * @return the joinRef
	 */
	public String getJoinRef()
	{
		return joinRef;
	}

	/**
	 * @param joinRef the joinRef to set
	 */
	public void setJoinRef(String joinRef)
	{
		this.joinRef = joinRef;
	}

	/**
	 * @return the autoincrement
	 */
	public Boolean getAutoincrement()
	{
		return autoincrement == null ? Boolean.FALSE : autoincrement;
	}
	
	public FieldType assign(FieldType f)
	{
		this.akey = f.akey;
		this.autoincrement = f.autoincrement;
		this.field = f.field;
		this.joinRef = f.joinRef;
		this.key = f.key;
		this.sqlName = f.sqlName;
		this.sqlNameRef = f.sqlNameRef;
		this.type = f.type;
		this.property = f.property;
		return this;
	}

	/**
	 * @return the sort
	 */
	public Integer getSort()
	{
		return sort;
	}

	/**
	 * @return the sortType
	 */
	public SortType getSortType()
	{
		return sortType;
	}

	/**
	 * @return the operator
	 */
	public String getOperator()
	{
		return operator;
	}
    public Boolean getDkey() {
		return dkey;
	}

	public void setDkey(Boolean dkey) {
		this.dkey = dkey;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator)
	{
		this.operator = operator;
	}

    public Boolean getKeyGenerator()
	{
		return keyGenerator == null ? false : keyGenerator;
	}

	public void setKeyGenerator(Boolean keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}
}
