package vietpot.server.Database.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import vietpot.server.ErrorLogger;
import vietpot.server.Database.BlobStoreEntryPoint;
import vietpot.server.Database.Database;
import vietpot.server.Database.DataAccessObjects.DAOs;
import vietpot.shared.Variables.MerchandiseSellType;
import vietpot.shared.Exceptions.DatabaseCorruptionException;
import vietpot.shared.ServerClientObjects.ClientReceive.AttributeDTO;
import vietpot.shared.ServerClientObjects.ClientReceive.CategoryDTO;
import vietpot.shared.ServerClientObjects.ClientReceive.CategoryTreeDTO;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@Entity
public class Merchandise implements Serializable, EntityInterface, EntityDeleteInterface, 
	EntityUpdateInterface<Merchandise>
{
	private static final long serialVersionUID = 1L;

	public enum Visibility
	{
		Visible, // Merchandise is visible and contains visible offers
		Dead, // Merchandise has no offers and never will
		Empty, // Merchandise has no offers but it may have them later.
	}

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Key ID;

	private @Column(nullable = false) Long accesses;
	private @Column(nullable = false) List<Text> description;
	private @Column(nullable = false) List<String> keywords;
	private @Column(nullable = false) List<String> searchTags;
	private @Column(nullable = false) Long minPrice;
	private @Column(nullable = false) List<String> name;
	private @Column(nullable = false) List<String> subname;
	private @Column(nullable = false) BlobKey photo;
	private @Column(nullable = false) Double popularity;
	private @Column(nullable = false) Set<MerchandiseSellType> discountTypes; // only current or future offers are applied
	
	// each LIKE "'attribute internal name'='localization':'attribute value'"
	private @Column(nullable = false) List<String> attrValues;
	
	private @Column(nullable = false) Set<Key> ancestors; // all categories that include this merchandise
	private @Column(nullable = false) Set<Key> categories; // lowest level categories that include this merchandise
	private @Column(nullable = false) Set<Key> suppliers;
	private @Column(nullable = false) Visibility visibility;
	private @Column(nullable = false) Date creationTime;
	private @Version Long version;

	protected Merchandise()
	{
	}

	// constructors with ID only contain fields that are updatable and exclusively changed from SpreadSheets
	public Merchandise(Key ID, List<String> name, List<String> subName, List<String> keywords, BlobKey photo, 
			List<Text> description, List<String> attrValues, Set<Key> ancestors, Set<Key> categories, Long version)
	{
		this.ID = ID;
		this.name = name;
		this.subname = subName;
		this.keywords = keywords;
		this.photo = photo;
		this.description = description;
		this.attrValues = attrValues;
		this.ancestors = ancestors;
		this.categories = categories;
		this.version = version;
		
		computeSearchTags();
		
		// discount types, suppliers, visibility to be updated manually
	}
	
	// constructor for creating new Merchandise which will be stored afterwards
	public Merchandise(List<String> name, List<String> subName, List<String> keywords, BlobKey photo, 
			List<Text> description, List<String> attrValues, Set<Key> ancestors, Set<Key> categories)
	{
		this.name = name;
		this.subname = subName;
		this.keywords = keywords;
		this.photo = photo;
		this.description = description;
		this.attrValues = attrValues;
		this.ancestors = ancestors;
		this.categories = categories;
		
		computeSearchTags();
		
		this.accesses = new Long(0);
		this.minPrice = new Long(0);
		this.popularity = new Double(0);
		this.version = new Long(0);
		
		// discount types, suppliers, visibility, creation time to be set later
	}
	
	private void computeSearchTags()
	{
		this.searchTags = new ArrayList<String>();
		parseListIntoSearchTags(getName());
		parseListIntoSearchTags(getSubname());
		parseListIntoSearchTags(getKeywords());
	}
	
	private void parseListIntoSearchTags(List<String> list)
	{
		for(String s : list)
		{
			int index = s.indexOf(':') + 1;
			String locale = s.substring(0, index);
			String[] splitted = s.substring(index).split("\\s");
			for(String ss : splitted)
			{
				if((ss != null) && (!ss.isEmpty()))
				{
					this.searchTags.add(locale + Database.normaliseSearchTag(ss));
				}
			}
		}
	}
	
	@Deprecated
	public void setAdditionalFieldsDefault()
	{
		this.discountTypes = EnumSet.allOf(MerchandiseSellType.class);
		this.suppliers = categories;
		this.visibility = Visibility.Visible;
	}

	public Key getID()
	{
		return ID;
	}
	
	public void setSuppliers(Set<Key> suppliers)
	{
		this.suppliers = suppliers;
	}

	public List<String> getName()
	{
		return name;
	}
	
	public List<String> getSubname()
	{
		return subname;
	}

	public void setSubname(List<String> subname)
	{
		this.subname = subname;
	}

	public List<String> getKeywords()
	{
		return keywords;
	}

	public BlobKey getPhoto()
	{
		return photo;
	}

	public void setPhoto(BlobKey photo)
	{
		this.photo = photo;
	}

	public List<Text> getDescription()
	{
		return description;
	}

	public void setDescription(List<Text> description)
	{
		this.description = description;
	}

	public Double getPopularity()
	{
		return popularity;
	}

	public void setPopularity(Double popularity)
	{
		this.popularity = popularity;
	}

	public Set<Key> getCategories()
	{
		return categories;
	}

	public List<String> getAttrValues()
	{
		return attrValues;
	}

	public Long getAccesses()
	{
		return accesses;
	}

	public void setAccesses(Long accesses)
	{
		this.accesses = accesses;
	}

	public Long getMinPrice()
	{
		return minPrice;
	}

	public void setMinPrice(Long minPrice)
	{
		this.minPrice = minPrice;
	}

	public Set<Key> getSuppliers()
	{
		return suppliers;
	}

	public Set<MerchandiseSellType> getDiscountTypes()
	{
		return discountTypes;
	}

	public void setDiscountTypes(Set<MerchandiseSellType> discountTypes)
	{
		this.discountTypes = discountTypes;
	}

	public Set<Key> getAncestors()
	{
		return ancestors;
	}

	public void setAncestors(Set<Key> ancestors)
	{
		this.ancestors = ancestors;
	}

	public Visibility getVisibility()
	{
		return visibility;
	}

	public void setVisibility(Visibility visibility)
	{
		this.visibility = visibility;
	}

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public Long getVersion()
	{
		return version;
	}

	public void setVersion(Long version)
	{
		this.version = version;
	}
	
	public List<String> getSearchTags()
	{
		return searchTags;
	}

	public void setSearchTags(List<String> searchTags)
	{
		this.searchTags = searchTags;
	}
	
	public vietpot.shared.ServerClientObjects.ClientReceive.MerchandiseDTO toDTO()
	{
		Offer minOffer = DAOs.offer.getLiveCurrentMinOfferByMerchandise(getID(), new Date());
		if(minOffer == null)
		{
			// something went terribly wrong here
			DatabaseCorruptionException dce = new DatabaseCorruptionException(DAOs.merchandise.getEntityName(), 
					KeyFactory.keyToString(getID()), 
					"OfferDAO.getLiveCurrentMinOfferByMerchandise should always return a non-null offer.");
			ErrorLogger.logThrowable("", dce);
			return null;
		}
		else
		{
			return new vietpot.shared.ServerClientObjects.ClientReceive.MerchandiseDTO(
					KeyFactory.keyToString(getID()), 
					getName(), 
					getSubname(), 
					getMinPrice(), 
					BlobStoreEntryPoint.getServingURL(getPhoto()),
					KeyFactory.keyToString(minOffer.getSupplier()),
					KeyFactory.keyToString(minOffer.getID())
			);
		}
	}
	
	public vietpot.shared.ServerClientObjects.ClientReceive.MerchandiseDetailDTO toDetailedDTO()
	{
		Map<String, List<String>> attrMapping = new HashMap<String, List<String>>();
		for(String attr : getAttrValues())
		{
			// assuming the right format
			int indexOfEq = attr.indexOf((int)'=');
			String attrName = attr.substring(0, indexOfEq);
			String attrValue = attr.substring(indexOfEq) + 1;
			List<String> values = attrMapping.get(attrName); 
			if(values == null)
			{
				values = new ArrayList<String>();
				attrMapping.put(attrName, values);
			}
			values.add(attrValue);
		}
		List<AttributeDTO> attrs = new ArrayList<AttributeDTO>();
		for(Entry<String, List<String>> entry : attrMapping.entrySet())
		{
			Attribute a = DAOs.attribute.getByName(entry.getKey());
			if(a != null)
			{
				attrs.add(new AttributeDTO(KeyFactory.keyToString(a.getID()), a.getName(), entry.getValue(), 
						a.getPreffix(), a.getSuffix()));
			}
			else
			{
				DatabaseCorruptionException dce = new DatabaseCorruptionException(DAOs.merchandise.getEntityName(),
						KeyFactory.keyToString(getID()), "Attribute with the internal name '" + entry.getKey() + 
						"' was not found in the database.");
				ErrorLogger.logThrowable("", dce);
			}
		}
		return new vietpot.shared.ServerClientObjects.ClientReceive.MerchandiseDetailDTO(
				Database.toStrings(getDescription()), getKeywords(), attrs, categoryPathsExport());
	}

	public vietpot.shared.ServerClientObjects.ClientReceive.ShoppingCart.MerchandiseDTO 
		toSCDTO(Offer chosenOffer, Long amount)
	{
		return new vietpot.shared.ServerClientObjects.ClientReceive.ShoppingCart.MerchandiseDTO(
				KeyFactory.keyToString(getID()), getName(), getSubname(), chosenOffer.toSCDTO(), amount);
	}

	public CategoryTreeDTO categoryPathsExport()
	{
		CategoryTreeDTO result = new CategoryTreeDTO(new ArrayList<Queue<CategoryDTO>>());
		for(Key parentKey : getCategories())
		{
			Category parentCat = DAOs.category.getByID(parentKey);
			if(parentCat != null)
			{
				result.getCategoryTree().addAll(parentCat.categoryTreeExport(false));
			}
			else
			{
				DatabaseCorruptionException dce = new DatabaseCorruptionException(DAOs.merchandise.getEntityName(),
						KeyFactory.keyToString(getID()), "Parent category with ID '" + parentKey + 
						"' was not found in the database.");
				ErrorLogger.logThrowable("", dce);
			}
		}
		return result;
	}
	
	@Override
	public void update(Merchandise newerEntityVersion)
	{
		this.name = newerEntityVersion.name;
		this.subname = newerEntityVersion.subname;
		this.photo = newerEntityVersion.photo;
		this.attrValues = newerEntityVersion.attrValues;
		this.ancestors = newerEntityVersion.ancestors;
		this.categories = newerEntityVersion.categories;
		this.description = newerEntityVersion.description;
		this.keywords = newerEntityVersion.keywords;
		this.searchTags = newerEntityVersion.searchTags;
		this.discountTypes = newerEntityVersion.discountTypes;
		this.suppliers = newerEntityVersion.suppliers;
		this.version = newerEntityVersion.version;
		this.visibility = newerEntityVersion.visibility;
	}
	
	@Override
	public boolean canKill()
	{
		List<Offer> currentOrFutureOffers = DAOs.offer.getLiveCurrentAndFutureByMerchandise(getID(), new Date());
		if((currentOrFutureOffers != null) && (currentOrFutureOffers.size() > 0))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean kill()
	{
		if(canKill())
		{
			this.visibility = Visibility.Dead;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isHidden()
	{
		return visibility == Visibility.Dead;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Merchandise other = (Merchandise) obj;
		if(ID == null)
		{
			if(other.ID != null) return false;
		}
		else if(!ID.equals(other.ID)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Merchandise [ID=" + ID + "]";
	}
}