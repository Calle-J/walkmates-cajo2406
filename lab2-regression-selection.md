# Lab 2 Regression Selection

**Group:** Carl Jonsson & Adam Persson

---

## Activity 4.3 Regression selection

### Test case selection

**Relevant tests to recent changes are:**
- **``freeShelterVolunteerListing()``** because a volunteer listing should always cost 0.00 SEK.
- **``shortWalkPrice()``** to secure that the price for a normal week day not has been modified by mistake.
- **``surchargeBountaryVerification()``** to ensure that the date logic doesn't affect the surcharge boundary.
- **``clearlyOverNightBooking()``** to verify the calculation of overnight-surcharge and weekend-surcharge.

**Low-risk tests:**
All the different Null tests. These tests are placed as low-risk because they only test for null values and
do not affect the actual calculation of the price.

### Prioritization
| Priority | Test Case                       |
|----------|---------------------------------|
| High     | freeShelterVolunteerListing()   |
| High     | shortWalkPrice()                |
| High     | surchargeBountaryVerification() |
| High     | clearlyOverNightBooking()       |
| Low      | All the different Null tests.   |
