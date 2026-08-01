# Family Locator & Safety System

A secure real-time family tracking application built with **Kotlin (Jetpack Compose)** for Android and **Django REST Framework** for the backend. The application allows family members to create private groups, share live locations, receive safety notifications, and manage geofenced zones.

---

## Features

- Google Sign-In authentication
- Secure JWT authentication
- Create and manage family groups
- Invite and remove family members
- Real-time live location sharing
- Google Maps integration
- Geofencing (Safety Zones)
- Instant notifications when a member enters or exits a Safety Zone
- Emergency SOS with live location
- Background location tracking
- Location history
- Push notifications
- Battery-optimized location updates
- Secure REST APIs
- PostgreSQL database

---

## Technology Stack

### Android
- Kotlin
- Jetpack Compose
- MVVM Architecture
- Google Maps SDK
- Fused Location Provider
- Firebase Cloud Messaging

### Backend
- Django
- Django REST Framework
- PostgreSQL
- JWT Authentication

---

## System Workflow

1. User signs in using Google.
2. User creates or joins a family group.
3. Family members share live locations securely.
4. Members can create Safety Zones (Geofences).
5. Notifications are sent when someone enters or leaves a Safety Zone.
6. Emergency SOS shares the user's live location with family members.
7. Live locations and group data are synchronized through the Django backend.

---

## Core Modules

### Authentication
- Google Sign-In
- JWT Authentication
- Secure user sessions

### Family Groups
- Create groups
- Join groups
- Invite members
- Remove members

### Live Location
- Real-time location updates
- Background location synchronization
- Google Maps visualization

### Safety Zones
- Create geofences
- Edit Safety Zones
- Delete Safety Zones
- Entry notifications
- Exit notifications

### Emergency
- One-tap SOS
- Live location sharing
- Emergency notifications

### Notifications
- Push notifications
- Geofence alerts
- SOS alerts
- Group activity updates

---

## Security

- JWT Authentication
- Secure REST APIs
- Protected user data
- Encrypted communication
- Permission-based access

---

## Future Enhancements

- Location history timeline
- Driving reports
- Speed alerts
- Battery status sharing
- Crash detection
- Route playback
- Multi-device support

---

## Author

**Usmana Zulfiqar**

Full-Stack Software Engineer

Kotlin • Jetpack Compose • Flutter • Django REST Framework • PostgreSQL
