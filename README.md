# Lab Management App (University Project)

A **desktop lab management system** that helps instructors/admins manage computers in a university laboratory.

This repository contains two main parts:

1. **Instructor / Admin UI (JavaFX)** — the “control panel” used by the instructor to discover machines, send commands, stream screens, remote control, transfer files, and more.
2. **Client Agent (Windows)** — runs on each lab computer and listens for commands from the instructor UI.

> ⚠️ Academic/learning project. Some paths and scripts are Windows-specific and may need small adjustments to run on your environment.

---

## Features (high level)

- **Device discovery & LAN view** (based on IP range + room id)
- **Wake-on-LAN (WOL)** to power on lab PCs (requires MAC addresses)
- **Screen streaming** (TCP/UDP)
- **Remote control** (send mouse/keyboard events)
- **File transfer / file collection**
- **Lab actions** (examples implemented on client):
  - Shutdown
  - Freeze / Unfreeze (overlay + disable input)
  - Enable/disable keyboard/mouse (Windows `pnputil`)
  - USB block/unblock (Windows registry)

---

## Tech Stack

- Java **20**
- JavaFX (**OpenJFX 20**) + some Swing utilities
- TCP/UDP networking
- Windows batch scripts for proxy/website allow-list (client side)

---

## Repository Structure

```
LabManagementAppUI/
  ├─ src/main/java/...            # JavaFX instructor application
  ├─ src/main/resources/...       # UI resources + logging config
  ├─ config.txt                   # multicast + room + IP range (generated/used by the UI)
  ├─ MACAddresses.txt             # ip:mac mapping (used for WOL)
  ├─ ControlConfig.txt            # last controlled machine (simple config)
  └─ pom.xml                      # Maven build for JavaFX app

University-Project-Client-streaming-fixes/
  └─ client/
      ├─ src/...                  # Client agent source (Windows-focused)
      ├─ Config.properties        # contains server-ip (teacher machine IP)
      └─ *.bat                    # allow/unblock proxy scripts (Windows)
```

---

# Part 1 — Run the Instructor UI (JavaFX)

## Prerequisites
- **JDK 20**
- **Maven**
- (Recommended) IntelliJ IDEA

Check:
```bash
java -version
mvn -version
```

## Run (recommended)
From the repository root:

```bash
cd LabManagementAppUI
mvn clean javafx:run
```

The JavaFX Maven plugin is configured to run:
- `com.LabManagementAppUI.Main`

## Build a jar (optional)
```bash
cd LabManagementAppUI
mvn clean package
```

---

## Configuration Files (Instructor Side)

The instructor UI reads/writes these files in the **LabManagementAppUI** directory:

### `config.txt`
Format:
```
<multicastAddress>:<roomId>:<ipRangeFrom>:<ipRangeTo>
```

Example:
```
239.0.0.1:12:192.168.2.1:192.168.2.20
```

### `MACAddresses.txt`
Format:
```
<ip>:<mac>
```

Used for Wake-on-LAN. If MAC addresses are missing, you may need to:
- ping the target machine first (so ARP table has the entry)
- then refresh/save MAC addresses from the UI (depending on your flow)

### `ControlConfig.txt`
A small helper config (e.g., stores the current/last selected IP).

---

# Part 2 — Run the Client Agent (on lab PCs)

## Important notes
- The client code includes Windows-only commands (`pnputil`, `reg`, etc.).
- Best run on **Windows 10/11** machines in the lab.

## Step 1 — Copy client folder to the lab PC
Copy this folder to each lab computer:
```
University-Project-Client-streaming-fixes/client
```

## Step 2 — Set the teacher/instructor IP
Edit:
```
University-Project-Client-streaming-fixes/client/src/Config.properties
```

Example:
```properties
server-ip=192.168.2.11
```

> If the file doesn’t exist, the client creates a default one on first run.

## Step 3 — Fix the Freeze image path (recommended)
In:
```
client/src/MiniServices/Screen.java
```

There is a hard-coded path to `wait.jpg`.  
Update it to a **relative path** (recommended), e.g. load `wait.jpg` from the project folder.

---

## Run the Client (easy way: IntelliJ)

1. Open IntelliJ
2. Open the folder:
   `University-Project-Client-streaming-fixes/client`
3. Add libraries (if IntelliJ asks):
   - `snappy-java-1.1.10.3.jar`
   - `thumbnailator-0.4.2-all.jar`
4. Run:
   - `Main.java` (starts the command receiver)

---

## Network Ports Used

Both instructor and client share these ports (defined in `IPorts`):

- `50000` — command / tokens channel
- `50011` — stream
- `50002` — remote control
- `50003` — file transfer
- `50012` — UDP stream

✅ Make sure Windows Firewall allows inbound/outbound TCP/UDP on these ports.

---

## Quick Smoke Test (Checklist)

- [ ] Instructor UI starts (`mvn javafx:run`)
- [ ] Client runs on a lab PC (starts without crashing)
- [ ] Both machines are on the same network/subnet
- [ ] Firewall ports are open (see ports section)
- [ ] `config.txt` has correct IP range
- [ ] Commands like **Shutdown / Freeze / Unfreeze** reach the client

---

## Troubleshooting

### JavaFX app doesn’t start
- Verify you’re using **JDK 20**.
- Re-run:
  ```bash
  mvn -v
  java -version
  ```

### WOL doesn’t work
- Ensure `MACAddresses.txt` has correct `ip:mac`.
- Try pinging the machine first to populate ARP.
- WOL may require BIOS/OS settings: “Wake on LAN enabled”.

### Freeze shows no image / crashes
- Fix the hard-coded `wait.jpg` path in `Screen.java`.

### Nothing reaches the client
- Check Windows Firewall rules.
- Ensure teacher IP in `Config.properties` is correct.
- Confirm ports are not blocked.

---

## Author

Mohammad Alsheikh
