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

import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class {@link PtuFlexOffer}: This class is a representation of a FlexOffer.
 */
@Entity
@Table(name = "PTU_FLEXOFFER")
public class PtuFlexOffer extends Exchange {

    @Column(name = "POWER", precision = 18, scale = 0)
    private BigInteger power;

    @Column(name = "PRICE", precision = 18, scale = 4)
    private BigDecimal price;

    @Column(name = "FLEXREQUEST_SEQUENCE", nullable = false)
    private Long flexRequestSequence;

    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    public BigInteger getPower() {
        return power;
    }

    public void setPower(BigInteger power) {
        this.power = power;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getFlexRequestSequence() {
        return flexRequestSequence;
    }

    public void setFlexRequestSequence(Long flexRequestSequence) {
        this.flexRequestSequence = flexRequestSequence;
    }

    public LocalDateTime getExpirationDate() {
        if (expirationDate == null) {
            return null;
        }
        return LocalDateTime.fromDateFields(expirationDate);
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate == null) {
            this.expirationDate = null;
        } else {
            this.expirationDate = expirationDate.toDateTime().toDate();
        }
    }

    @Override
    public String toString() {
        return "PtuFlexOffer" + "[" +
                "power=" + power +
                ", price=" + price +
                ", flexRequestSequence=" + flexRequestSequence +
                "]";
    }
}
