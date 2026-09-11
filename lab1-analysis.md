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

## Activity 1.2 Bug analysis