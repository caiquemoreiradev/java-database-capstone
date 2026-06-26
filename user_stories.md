# Smart Clinic System User Stories

## Exercise 2: Admin User Stories

### Admin Story 1: Secure Login
**Title:**
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. The login page must provide secure input fields masked for passwords.
2. The system must authenticate entered credentials against the MySQL database.
3. Upon successful authentication, the admin must be redirected directly to the Admin Dashboard.

**Priority:** High
**Story Points:** 3
**Notes:**
- Account access should temporarily lock out after 5 consecutive failed attempts to prevent brute-force attacks.

---

### Admin Story 2: Platform Logout
**Title:**
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. A clear "Log Out" option must be visible on the Admin Dashboard navigation panel at all times.
2. Clicking logout must immediately destroy the current user session and invalidate tokens.
3. The system must securely redirect the user back to the public-facing login interface.

**Priority:** High
**Story Points:** 1
**Notes:**
- Browser back-button navigation should not re-authenticate the user after logging out.

---

### Admin Story 3: Onboard Medical Staff
**Title:**
_As an admin, I want to add doctors to the portal, so that they can manage their profiles and appointments._

**Acceptance Criteria:**
1. Provide an onboarding form requiring standard inputs (Name, Specialization, Contact Info, Email).
2. The form must perform validations ensuring the doctor's email is uniquely registered.
3. Successfully submitted profiles must generate a new entry inside the MySQL Database JPA schema.

**Priority:** High
**Story Points:** 5
**Notes:**
- An automated welcome email with initial temporary credentials should be dispatched to the doctor's email upon creation.

---

### Admin Story 4: Remove Medical Staff Profiles
**Title:**
_As an admin, I want to delete a doctor's profile from the portal, so that the clinic directory stays up to date._

**Acceptance Criteria:**
1. Provide a clear deletion option alongside individual doctor accounts inside the dashboard view.
2. Trigger an explicit confirmation warning modal prior to performing any system deletes.
3. Safely eliminate the profile or flags it as inactive within the MySQL storage module.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Check for existing active clinical appointments before permitting profile removal to prevent orphan schedule records.

---

### Admin Story 5: Monthly Operational Analytics
**Title:**
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. A dedicated database script or stored procedure must exist on the underlying MySQL database instance.
2. Executing the specific target query structure within the MySQL CLI must pull correct cumulative numbers grouped by month.
3. The data returns a precise breakdown without timing out under heavy transactional loads.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Ensure correct database indexing is placed over date and timestamp criteria elements to keep execution highly performant.

***

## Exercise 3: Patient User Stories

### Patient Story 1: Explore Directory Anonymously
**Title:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore my treatment options before registering._

**Acceptance Criteria:**
1. The public landing page must display an option to view clinical doctor directories.
2. Users can browse full active doctor profiles without triggering authentication walls.
3. All appointment scheduling features must remain hidden or locked until the profile logs in.

**Priority:** High
**Story Points:** 3
**Notes:**
- Unauthenticated traffic should hit read-optimized cache mechanisms to limit performance strains on database servers.

---

### Patient Story 2: Account Creation
**Title:**
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. A registration screen must provide email collection fields alongside double-entry password setups.
2. The portal validates complex password patterns (characters, numbers, casing) for security compliance.
3. Saved profiles create corresponding entries inside the MySQL relational model architecture safely.

**Priority:** High
**Story Points:** 5
**Notes:**
- Double registration actions using existing email profiles must gracefully flag explicit error handling hints back to users.

---

### Patient Story 3: Access Booking Management
**Title:**
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Portal includes standard consumer login interfaces parsing patient credentials.
2. Valid entries correctly route users into specialized consumer dashboards.
3. The dashboard aggregates complete past and current personal appointment elements seamlessly.

**Priority:** High
**Story Points:** 3
**Notes:**
- Dashboards must render seamlessly on cross-browser and adaptive mobile view layouts.

---

### Patient Story 4: Secure Portal Logout
**Title:**
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. Clear access endpoints to invoke instant portal logout tasks must sit clearly on navigation panels.
2. Session cookies and application authorization headers must instantly clean out upon activation.
3. Re-navigating back to booking views post-logout forces a redirect to sign-in paths.

**Priority:** High
**Story Points:** 1
**Notes:**
- Handles automated logouts if client browser terminals remain completely inactive beyond a 15-minute timeframe.

---

### Patient Story 5: Schedule Direct Consultations
**Title:**
_As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**
1. Logged-in view permits choosing active medical specialists from operational calendars.
2. Booking forms restrict entries strictly to open, unallocated 60-minute blocks.
3. Submitting selections commits reservations directly inside the app schema, blocking that time block out from other peers.

**Priority:** High
**Story Points:** 8
**Notes:**
- Concurrency logic checks must enforce that overlapping bookings on the same calendar block throw system exceptions instead of allowing duplicates.

---

### Patient Story 6: Track Upcoming Schedules
**Title:**
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Dashboard includes a prominently located list tracking chronological pending consultations.
2. Data details match correct timing metrics, professional identities, and localized clinical room locations.
3. Empty listings must show a clear placeholder message advising no pending appointments exist.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Appointments occurring inside the next 24-hour window should flag visually distinctive alert accents to inform the user.

***

## Exercise 4: Doctor User Stories

### Doctor Story 1: Schedule Management Access
**Title:**
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Provide a medical practitioner sign-in panel verifying unique provider credentials.
2. Redirect successfully authenticated sessions to a clean, practitioner-specific medical console dashboard.
3. Restrict dashboard access so it contains only records assigned to the logged-in doctor.

**Priority:** High
**Story Points:** 3
**Notes:**
- Enforces strict route guards to prevent general patients from accessing or guessing doctor-exclusive URL endpoints.

---

### Doctor Story 2: Terminate Practitioner Session
**Title:**
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. Medical dashboards must place prominent, single-click logout buttons within easy reach.
2. Ensure patient chart cache stores are wiped from memory spaces when sessions end.
3. Redirect instantly to clean public home screens upon completion.

**Priority:** High
**Story Points:** 1
**Notes:**
- Critical for hospital floor compliance, safeguarding sensitive clinical patient files from being seen on shared computers.

---

### Doctor Story 3: Calendar Overview
**Title:**
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Medical console must embed daily, weekly, and monthly responsive grid calendars.
2. Pull allocated patient appointment rows directly from MySQL models dynamically.
3. Clicking specific blocks must open quick overlay panels detailing foundational booking context.

**Priority:** High
**Story Points:** 5
**Notes:**
- Calendar items should support color-coding adjustments based on appointment status indicators (e.g., Confirmed, Pending, Completed).

---

### Doctor Story 4: Out-Of-Office Blockout
**Title:**
_As a doctor, I want to mark my unavailability, so that I can inform patients only the available slots._

**Acceptance Criteria:**
1. Interface must allow providers to select specific hours or complete calendar days to designate as unavailable.
2. Saved unavailabilities should immediately prevent corresponding blocks from showing up on patient-facing booking screens.
3. Existing bookings on blocked days must prompt explicit notification warnings to manage scheduling conflicts.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Blockouts must support recurring logic presets (e.g., setting a slot as unavailable every Tuesday morning).

---

### Doctor Story 5: Dynamic Profile Optimization
**Title:**
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Console incorporates dedicated profile setup interfaces for authenticated practitioners.
2. Updates made to specializations or addresses should instantly map through the repository logic.
3. Updated fields refresh live on public patient searches immediately after submission.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Specialized field shifts (like clinical licensing details) can be flagged to require secondary administrator verification if necessary.

---

### Doctor Story 6: Patient Records Overview
**Title:**
_As a doctor, I want to view the patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Clicking an upcoming appointment opens full details of the associated patient profile.
2. Fetch related history records smoothly across multi-database repository lookups (MySQL for metadata, MongoDB for past prescriptions).
3. Record views are locked down behind read-only authorization flags to prevent unintended modification.

**Priority:** High
**Story Points:** 5
**Notes:**
- Compliance with healthcare privacy standards requires logging an audit trail entry every time a patient's historical charts are accessed.
