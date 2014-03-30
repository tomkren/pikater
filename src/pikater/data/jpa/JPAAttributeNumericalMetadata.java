package pikater.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAAttributeNumericalMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private boolean isReal;
	private float min;
	private float max;
	private float modus;
	private float median;
	private float classEntropy;
	private float variance;
	private float avarage;

	public boolean isReal() {
		return isReal;
	}
	public void setReal(boolean isReal) {
		this.isReal = isReal;
	}

	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}

	public float getModus() {
		return modus;
	}
	public void setModus(float modus) {
		this.modus = modus;
	}

	public float getMedian() {
		return median;
	}
	public void setMedian(float median) {
		this.median = median;
	}

	public float getClassEntropy() {
		return classEntropy;
	}
	public void setClassEntropy(float classEntropy) {
		this.classEntropy = classEntropy;
	}

	public float getVariance() {
		return variance;
	}
	public void setVariance(float variance) {
		this.variance = variance;
	}

	public float getAvarage() {
		return avarage;
	}
	public void setAvarage(float avarage) {
		this.avarage = avarage;
	}
}
