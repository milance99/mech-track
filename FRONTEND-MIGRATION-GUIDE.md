# 🔐 MechTrack Authentication System Update - Frontend Migration Guide

## 📋 **What We Changed on the Backend**

We've upgraded from a simple JWT system to a **secure refresh token system** for better security and user experience. Here's what's different now:

---

## 🚨 **BREAKING CHANGES - What You Need to Know**

### **1. Login Response Format Changed**

**Before:** Login returned a single `token` field
**Now:** Login returns both `accessToken` and `refreshToken` fields

The response structure has completely changed:
- `token` → `accessToken` 
- `expiresAt` → `accessTokenExpiresAt`
- **NEW:** `refreshToken` field
- **NEW:** `refreshTokenExpiresAt` field

### **2. Token Expiration Times Changed**

**Before:** Tokens lasted 24 hours
**Now:** 
- **Access tokens:** 15 minutes (much shorter!)
- **Refresh tokens:** 7 days

### **3. New API Endpoints Available**

We've added two new endpoints:
- `POST /api/auth/refresh` - Get new tokens without re-login
- `POST /api/auth/logout` - Securely invalidate tokens

---

## 🔄 **How the New System Works**

### **Login Flow**
1. User logs in with username/password (same as before)
2. Backend returns **two tokens** instead of one:
   - **Access Token:** Short-lived (15 min) - use for API calls
   - **Refresh Token:** Long-lived (7 days) - use to get new access tokens

### **API Request Flow**
1. Use the **access token** for all API requests (same Authorization header)
2. When access token expires (every 15 minutes), use refresh token to get new ones
3. Continue using the new access token for API calls

### **Token Refresh Flow**
1. When access token expires, call `/api/auth/refresh` with the refresh token
2. Backend returns **brand new tokens** (both access and refresh)
3. Old tokens are automatically invalidated (security feature)
4. Use the new access token for subsequent API calls

### **Logout Flow**
1. Call `/api/auth/logout` with the refresh token
2. Backend invalidates the refresh token
3. User is securely logged out

---

## 📡 **API Endpoint Details**

### **1. Login (Modified)**
- **Endpoint:** `POST /api/auth/login`
- **Request:** Same as before (username/password)
- **Response:** **CHANGED** - now includes both tokens
- **What's Different:** Response structure completely changed

### **2. Token Refresh (NEW)**
- **Endpoint:** `POST /api/auth/refresh`
- **Purpose:** Get new tokens without re-login
- **Request:** Send current refresh token
- **Response:** Brand new access and refresh tokens
- **Security:** Old tokens are invalidated automatically

### **3. Logout (NEW)**
- **Endpoint:** `POST /api/auth/logout`
- **Purpose:** Securely invalidate tokens
- **Request:** Send current refresh token
- **Response:** Confirmation of logout
- **Security:** Refresh token is permanently invalidated

### **4. Token Validation (Unchanged)**
- **Endpoint:** `POST /api/auth/validate`
- **Purpose:** Check if access token is valid
- **No changes:** Works exactly the same as before

---

## ⚡ **Key Implementation Points**

### **Token Storage**
- **Store both tokens** securely on the frontend
- **Access token** is used for API calls
- **Refresh token** is used only for token refresh

### **Token Expiration Handling**
- **Access tokens expire every 15 minutes** (much more frequent than before)
- **Automatic refresh** should happen when access token expires
- **Refresh tokens expire after 7 days** (user needs to re-login after this)

### **Security Features We Implemented**
- **Token Rotation:** Every refresh gives you completely new tokens
- **Automatic Invalidation:** Old tokens become useless after refresh
- **Secure Logout:** Refresh tokens are permanently invalidated on logout
- **Short-lived Access Tokens:** Reduces security risk if token is compromised

### **Error Handling**
- **401 Unauthorized:** Access token expired → Try refresh
- **401 on Refresh:** Refresh token expired → Redirect to login
- **403 Forbidden:** User doesn't have permission (same as before)

---

## 🔧 **Migration Strategy**

### **Phase 1: Update Login Response Handling**
- Change how you parse the login response
- Store both tokens instead of just one
- Update token expiration tracking

### **Phase 2: Implement Token Refresh**
- Add logic to detect when access token expires
- Implement automatic token refresh using the refresh endpoint
- Handle refresh failures (redirect to login)

### **Phase 3: Add Logout Functionality**
- Call the new logout endpoint when user logs out
- Clear stored tokens after successful logout

### **Phase 4: Testing**
- Test login with new response format
- Test automatic token refresh (wait 15 minutes or manually expire token)
- Test logout functionality
- Test error scenarios (expired refresh token, network issues)

---

## 📚 **Resources Available**

### **API Documentation**
- **Swagger UI:** Available at `/swagger-ui.html` (updated with new endpoints)
- **Postman Collection:** `MechTrack-RefreshToken-Tests.postman_collection.json`
- **Testing Script:** `test-refresh-tokens.sh` (for manual testing)

### **Environment Variables (No Changes Needed)**
- All configuration is handled on the backend
- No new environment variables required for frontend

---

## 🚀 **Benefits of This Change**

### **Security Improvements**
- **Shorter exposure window:** Access tokens expire in 15 minutes
- **Token rotation:** New tokens on every refresh
- **Secure logout:** Tokens are properly invalidated

### **User Experience**
- **No more sudden logouts:** Automatic token refresh
- **Seamless experience:** User stays logged in for 7 days
- **Faster recovery:** No need to re-enter credentials frequently

### **Development Benefits**
- **Better error handling:** Clear distinction between expired access vs refresh tokens
- **Easier debugging:** Separate endpoints for different operations
- **Future-proof:** Industry standard refresh token pattern

---

## ❓ **Common Questions**

**Q: Do I need to change how I send API requests?**
A: Only the token field name changes from `token` to `accessToken`. Authorization header format stays the same.

**Q: How often will tokens refresh?**
A: Access tokens expire every 15 minutes, but you can refresh them automatically in the background.

**Q: What happens if the refresh token expires?**
A: User needs to log in again. This happens after 7 days of inactivity.

**Q: Can I test this without implementing everything?**
A: Yes! Use the Postman collection or test script to understand the flow first.

---

## 🆘 **Need Help?**

- **Swagger Documentation:** `/swagger-ui.html` - Complete API reference
- **Test Tools:** Use the provided Postman collection for hands-on testing
- **Questions:** Reach out if anything is unclear about the new flow

**The backend is fully implemented and tested - ready for frontend integration!** 🚀
