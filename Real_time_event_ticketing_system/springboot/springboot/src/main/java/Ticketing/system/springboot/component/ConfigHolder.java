package Ticketing.system.springboot.component;

import org.springframework.stereotype.Component;

/**
 * Component that holds the configuration settings for the ticketing system.
 */
@Component
public class ConfigHolder {
    private volatile int purchaseRate = 500; // Default purchase rate in milliseconds
    private volatile int releaseRate = 1000; // Default release rate in milliseconds
    private volatile int maxTicketCapacity = 100; // Default maximum ticket capacity

    /**
     * Default constructor for ConfigHolder.
     * Creates a new ConfigHolder with default settings.
     */
    public ConfigHolder() {}

    /**
     * Gets the maximum ticket capacity.
     *
     * @return the maximum ticket capacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum ticket capacity.
     * @param maxTicketCapacity the new maximum ticket capacity
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Gets the current purchase rate.
     *
     * @return the purchase rate in milliseconds
     */
    public int getPurchaseRate() {
        return purchaseRate;
    }

    /**
     * Sets the purchase rate.
     *
     * @param purchaseRate the new purchase rate in milliseconds
     */
    public void setPurchaseRate(int purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    /**
     * Gets the current release rate.
     *
     * @return the release rate in milliseconds
     */
    public int getReleaseRate() {
        return releaseRate;
    }

    /**
     * Sets the release rate.
     * @param releaseRate the new release rate in milliseconds
     */
    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;


    }
}
