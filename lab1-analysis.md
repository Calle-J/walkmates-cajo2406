# Lab 1 Analysis

**Group:** Carl Jonsson & Adam Persson

---

## Activity 1.1 Quality-attribute analysis

### FR-1.1 Registration
The first ISO/IEC 25010 quality characteristic we chose was **``3.4.4 user error protection``**. We chose this 
one because a user should not be able to register an account with invalid credentials.

The second quality characteristic we chose was **``3.4.8 self-descriptivness``**. We chose this one
because the system should provide appropriate/necessary information to the user during registration regarding
the required format of the input.

### FR-2.2 Capacity rule
The first quality characteristic we chose was **``3.1.2 functional correctness``**. We chose this one
because a seeker should not be able to take a job for a provider if the provider already has reached the
capacity limit for active bookings.

The second quality characteristic we chose was **``3.5.2 availability``**. We chose this one to ensure that
the provider's current capacity status is available when needed, so seekers are immediately notified when
a provider has reached their booking limit and cannot accept any new bookings.

### FR-4.3 Pricing
The first quality characteristic we chose was **``3.6.2 integrity``**. We chose this one because unauthorized
users should not be able to change/manipulate the price of a job/booking.

The second quality characteristic we chose was **``3.1.2 functional correctness``**. We chose this one because the 
price must be calculated accurately based on trust tiers and the job's duration.

### Testable quality requirement
A seeker with a trust tier of **``NEW``** applies for a **``DOG_WALK``** booking with a duration of 9 hours. The price 
should be calculated as follows:
- **``NEW``** tier: Platform fee of 15%
- **``DOG_WALK``** base rate: 80 SEK/hour
- **``overnight surcharge``**: 20%

Expected price: 80 x 9 = 720 -> 720 + 20% = 864 -> 864 + 15% = 993,6 SEK

The expected outcome is that the system should calculate a price of 993.6 SEK for the user.

## Activity 1.2 Bug analysis
The human error is a logic mistake, and the fault is usage of the wrong operator. 
The if-statement for rule number 2 uses ">" instead of ">=" as shown below.  

``` java
if (seekerActive > seeker.getMaxConcurrentBookings()) { ... }
```

The test level that should have caught this is the "Component (unit)" level during white box testing. 


## Activity 2.1 Equivalence Partitioning
| Input Field  | Partition                                           | Representative Value   | Expected Outcome |
|--------------|-----------------------------------------------------|------------------------|------------------|
| Email        | Valid, proper format                                | example@example.com    | Accepted         |
| Email        | Invalid, missing **``@``**                          | example.com            | Rejected         |
| Email        | Invalid, multiple **``@``**                         | test@@example.com      | Rejected         |
| Email        | Invalid, missing **``.``**                          | test@examplecom        | Rejected         |
| Email        | Invalid, missing local part                         | @example.com           | Rejected         |
| Email        | Invalid, too long local part                        | a........b@example.com | Rejected         |
| Display name | Valid, proper length and valid characters           | Adam-Persson           | Accepted         |
| Display name | Invalid, too short (<2 chars)                       | A                      | Rejected         |
| Display name | Invalid, too long (>40 chars)                       | Aaaaaaaaaaaaaaaaa...   | Rejected         |
| Display name | Invalid, contains digits                            | Adam123                | Rejected         |
| Display name | Invalid, contains special characters                | Adam:)                 | Rejected         |
| Phone number | Valid (Swedish format, 10 digits, starts with "07") | 0731231234             | Accepted         |
| Phone number | Valid (International, 12 digits, starts with "+46") | +46731231234           | Accepted         |
| Phone number | Invalid, swedish format, too long                   | 07312312312            | Rejected         |
| Phone number | Invalid, swedish format, too short                  | 073123123              | Rejected         |
| Phone number | Invalid, international, too long                    | +467312312345          | Rejected         |
| Phone number | Invalid, international, too short                   | +4673123123            | Rejected         |
