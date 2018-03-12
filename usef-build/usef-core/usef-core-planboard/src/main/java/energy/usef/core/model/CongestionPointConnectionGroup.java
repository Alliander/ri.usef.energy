/*
 * Copyright 2015-2016 USEF Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package energy.usef.core.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Subclass of {@link ConnectionGroup} representing connections grouped in a Congestion Point.
 */
@Entity
@DiscriminatorValue("CONGESTION_POINT")
public class CongestionPointConnectionGroup extends ConnectionGroup {

    /**
     * Default constructor.
     */
    public CongestionPointConnectionGroup() {
        // default constructor.
        super();
    }

    /**
     * {@inheritDoc}
     */
    public CongestionPointConnectionGroup(String usefIdentifer) {
        super(usefIdentifer);
    }

    /**
     * {@inheritDoc}
     */
    public CongestionPointConnectionGroup(String usefIdentifer, Boolean dayAheadAutoOrder, Boolean intraDayAutoOrder, Integer dayAheadPercentage) {
        super(usefIdentifer);
        this.dayAheadAutoOrder = dayAheadAutoOrder;
        this.intraDayAutoOrder = intraDayAutoOrder;
        this.dayAheadPercentage = dayAheadPercentage;
    }

    @Column(name = "DSO_DOMAIN")
    private String dsoDomain;

    @Column(name = "DAY_AHEAD_AUTO_ORDER")
    private Boolean dayAheadAutoOrder;

    @Column(name = "INTRA_DAY_AUTO_ORDER")
    private Boolean intraDayAutoOrder;

    @Column(name = "DAY_AHEAD_PERCENTAGE")
    private Integer dayAheadPercentage;

    public String getDsoDomain() {
        return dsoDomain;
    }

    public void setDsoDomain(String dsoDomain) {
        this.dsoDomain = dsoDomain;
    }

    public Boolean getDayAheadAutoOrder() {
        return dayAheadAutoOrder;
    }

    public void setDayAheadAutoOrder(Boolean dayAheadAutoOrder) {
        this.dayAheadAutoOrder = dayAheadAutoOrder;
    }

    public Boolean getIntraDayAutoOrder() {
        return intraDayAutoOrder;
    }

    public void setIntraDayAutoOrder(Boolean intraDayAutoOrder) {
        this.intraDayAutoOrder = intraDayAutoOrder;
    }

    public Integer getDayAheadPercentage() {
        return dayAheadPercentage;
    }

    public void setDayAheadPercentage(Integer dayAheadPercentage) {
        this.dayAheadPercentage = dayAheadPercentage;
    }

    @Override
    public String toString() {
        return "CongestionPointConnectionGroup" + "[" +
                "dsoDomain='" + dsoDomain + "'" +
                "]";
    }
}
