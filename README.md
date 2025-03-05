![Sustaina Banner](app/src/main/res/drawable/banner_sustaina.png)

# Sustaina

## Brief Description

Sustaina is a real-time platform that leverages AI to crowdsource and visualize environmental issues. Through gamification, it fosters community engagement by rewarding users for reporting and resolving sustainability challenges, driving collective action for a greener future.

## Project Goal/Purpose

### **Together for Sustainability**

By spreading awareness and fostering collaboration, we aim to drive global participation in eco-friendly initiatives and creative problem-solving for a better tomorrow.

## Main Features

- **Interactive Map Interface** →
  - Displays garbage-prone hotspots based on user reports and AI analysis.
  - Provides real-time updates on cleaned areas and remaining waste density.
- **AI-Powered Trash Detection** →
  - Utilizes Computer Vision to identify and categorize different types of trash.
  - Estimates the density and distribution of waste in a given area.
- **AI-Based Clean Area Verification** →
  - Confirms whether an area has been properly cleaned using before-and-after image analysis.
  - Evaluates the effectiveness of waste collection by measuring density reduction.
- **Gamification System** →
  - Users earn XP, badges, and eco-coins for their cleanup efforts.
  - Potential integration with real-world incentives, such as discount coupons or rewards from eco-friendly brands.
- **Community Cleanup Events** →
  - Organizations can host large-scale cleanups, visible on the app’s map.
  - Users can join and track local cleanup initiatives, fostering community engagement.
- **Problem Identification & Reporting System** →
  - Report garbage-prone locations through GPS location tagging.
- **Proposal System for Innovative Green Solutions** →
  - Users can submit detailed proposals for sustainable solutions.

## Tech Stack

| Feature | Technology               |
| --- |--------------------------|
| Frontend | Kotlin (Jetpack Compose) |
| Backend | Firebase                 |
| Authentication | Firebase Auth            |
| AI Object Detection | Tensoflow and YOLO11     |
| Prototyping & UI Design | Figma                    |

---
### UP Cebu Hackathon 2025 - Sustainability Theme

## Interactive Map Interface
Our app features a dynamic and interactive map that provides users with real-time insights into waste management efforts in their area.

|               Interactive Map Hotspot                |            Interactive Map Clean-up Events            |
|:----------------------------------------------------:|:-----------------------------------------------------:|
| <img src="readme_res/map_interface.png" width="300"> | <img src="readme_res/map_interface2.png" width="300"> |


- **Garbage-Prone Hotspots** – The map highlights areas with significant waste accumulation based on user reports and AI-driven analysis. This allows authorities, organizations, and volunteers to identify and address problem areas effectively.

- **Real-Time Cleanup Updates** – Users can track ongoing cleanup activities and see live updates on cleaned areas, including the estimated reduction in waste density.

- **Waste Density Visualization** – Th app add information of the volume and distribution of waste based on crowdsourcing, helping users and local governments prioritize cleanup efforts.

## AI Powered Trash Detection
Our AI-driven system utilizes Computer Vision and Machine Learning to automatically detect and classify various types of waste, improving sorting efficiency.

<div style="display: flex; justify-content: center; gap: 10px;">
    <img src="readme_res/ai_sample1.jpg" width="400">
    <img src="readme_res/ai_sample2.jpg" width="400">
</div>


- **Smart Trash Recognition** – Using an image-based detection system, the AI classify trashes through different labels.
<details>
  <summary><b>Click to expand: Waste Categories Table</b></summary>

| Super Category | Category | Notes |
  |--------------|--------------|------|
| Aluminium foil | Aluminium foil | - |
| Battery | Battery | - |
| Blister pack | Aluminium blister pack | Containers used to store capsules (e.g. pills) |
| Blister pack | Carded blister pack | Paper-back package |
| Bottle | Clear plastic bottle | Water and soft drink bottles made of PET |
| Bottle | Glass bottle | Includes beer and wine bottles |
| Bottle | Other plastic bottle | Opaque or translucent. Generally made of HDPE. Includes detergent bottles |
| Bottle cap | Plastic bottle cap | - |
| Bottle cap | Metal bottle cap | - |
| Broken glass | Broken glass | - |
| Can | Aerosol | - |
| Can | Drink can | Aluminium soda can |
| Can | Food can | Steel can |
| Carton | Corrugated carton | Includes cardboard boxes |
| Carton | Drink carton | Tetrapak composites |
| Carton | Egg carton | - |
| Carton | Meal carton | Includes sandwich boxes, paper plates, take-out boxes |
| Carton | Pizza box | - |
| Carton | Toilet tube | - |
| Carton | Other carton | Paperboard boxes |
| Cigarette | Cigarette | Cigarette butts |
| Cup | Paper cup | - |
| Cup | Disposable plastic cup | Generally made of PET |
| Cup | Foam cup | Polystyrene Cup |
| Cup | Glass cup | - |
| Cup | Other plastic cup | Reusable plastic cups, thicker than disposable ones |
| Food waste | Food waste | - |
| Glass jar | Glass jar | - |
| Lid | Plastic lid | Includes cup lids |
| Lid | Metal lid | Generally glass jar lids |
| Paper | Normal paper | - |
| Paper | Tissues | - |
| Paper | Wrapping paper | - |
| Paper | Magazine paper | Plastified paper used in catalogues |
| Paper bag | Paper bag | Brown bag |
| Plastic bag & wrapper | Garbage bag | - |
| Plastic bag & wrapper | Single-use carrier bag | - |
| Plastic bag & wrapper | Polypropylene bag | Reusable bags |
| Plastic bag & wrapper | Plastic Film | May be transparent or opaque. Includes bread bags, cereal bags and produce bags |
| Plastic bag & wrapper | Six pack rings | - |
| Plastic bag & wrapper | Crisp packet | So common that it needs its own category |
| Plastic bag & wrapper | Other plastic wrapper | Can be made of aluminium. Includes candy wrappers, retort pouches and yoghurt lids |
| Plastic container | Spread tub | Includes margarine tubs and yoghurt pots |
| Plastic container | Tupperware | HDPE microwavable tub |
| Plastic container | Disposable food container | Includes black trays and PET containers |
| Plastic container | Foam food container | Styrofoam takeaway boxes |
| Plastic gloves | Plastic gloves | - |
| Plastic utensils | Plastic utensils | - |
| Pop tab | Pop tab | - |
| Rope | Rope | Includes fishing nets |
| Scrap metal | Scrap metal | Includes all metal except cans |
| Shoe | Shoe | - |
| Squeezable tube | Squeezable tube | Includes toothpaste and glue tubes |
| Straw | Plastic straw | - |
| Straw | Paper straw | - |
| Styrofoam piece | Styrofoam piece | - |
| Other plastic | Other plastic | Includes other objects or fragments made of plastic |
| Unlabeled litter | Unlabeled litter | Unknown object of unknown material. Any ambiguous object. |

Source: [TACO Trash Dataset](http://tacodataset.org/taxonomy)

</details>



- **Waste Density & Distribution Analysis** – Users can take a photo of waste, and the AI will instantly provide classification, ensuring proper waste segregation. Then the AI classifies the trashes based on its label.

## Smart Waste Segregation
To promote responsible waste disposal, the app provides a comprehensive AI-powered waste segregation system that guides users on how to properly sort and dispose of their trash.

- **AI Image-Based Classification**  – Users can upload or scan an image of waste items, and the app will automatically determine the correct category.

- **Step-by-Step Disposal Guide ** – Provides detailed disposal instructions tailored to local regulations, ensuring compliance with waste management policies.

## Gamification and Reward System
Encouraging environmental responsibility through an engaging reward system that motivates users to actively participate in cleanup efforts.

<p align="center">
    <img src="readme_res/gamification.png" alt="Gamification Image">
</p>

- **XP & Badge System** – Users earn experience points (XP) and unlock badges for activities such as correctly segregating waste, participating in cleanup drives, and reporting waste-prone areas.

- **Eco-Coins & Real-World Incentives** – Users can accumulate eco-coins that may be redeemable for discounts, vouchers, or rewards from eco-friendly brands and participating businesses or organizations.

## Community Clean-up Events
The app provides an organized platform for local and large-scale cleanup initiatives, making it easier for volunteers and environmental organizations to connect and collaborate.

<div style="display: flex; justify-content: center;">
  <div style="gap: 10px;">
      <img src="readme_res/campaign_event.png" width="200">
      <img src="readme_res/hotspot_event.png" width="200">
      <img src="readme_res/dashboard.png" width="200">
  </div>
</div>


- **Event Hosting & Registration** – Environmental groups, schools, and local governments can create and manage cleanup events, inviting users to participate through the app.

- **Live Event Tracking** – Users can view an interactive map of upcoming and ongoing cleanup drives, enabling them to join efforts in their local community.

- **Volunteer Progress Tracking** – Participants can log their contributions, track the amount of waste collected, and receive recognition for their efforts through rewards and badges.

## Problem Identification & Reporting System
Empowering communities with a user-driven reporting system to alert authorities and environmental groups about garbage accumulation issues.

- **GPS-Based Location Tagging** – Users can report garbage-prone areas by providing precise coordinates for cleanup efforts.

- **Description Submission** – Users can put descriptions of reported waste problems, allowing for better assessment and prioritization.

- **Direct Notifications to Cleanup Authorities **– The system sends real-time alerts to local waste management teams and environmental organizations, expediting cleanup responses.

## Proposal System for Innovative Green-Solutions
Encouraging users to contribute creative and sustainable solutions to environmental challenges by providing a dedicated proposal submission platform.

<div style="display: flex; justify-content: center;">
  <div style="gap: 10px;">
      <img src="readme_res/solution.png" width="200">
  </div>
</div>

- **User-Submitted Green Initiatives**– Community can submit solution proposals for new environmental projects, upcycling solutions, or eco-friendly alternatives to common waste materials.

- **Community Voting & Feedback** – Users can vote and comment on submitted proposals, promoting collaboration and idea-sharing.

- **Support for Project Implementation** – The most promising solutions may be highlighted for funding, partnerships, or integration into real-world waste management programs.

## References
### [TACO Trash Dataset](http://tacodataset.org/ "TACO Datase")
Why TACO?
Humans have been trashing planet Earth from the bottom of Mariana trench to Mount Everest. Every minute, at least 15 tonnes of plastic waste leak into the ocean, that is equivalent to the capacity of one garbage truck. We have all seen the impact of this behaviour to wildlife on images of turtles choking on plastic bags and birds filled with bottle caps. Recent studies have also found microplastics in human stools. These should be kept in the recycling chain not in our food chain. It is time for a revolution.

We believe AI has an important role to play. Think of drones surveying trash, robots picking up litter, anti-littering video surveillance and AR to educate and help humans to separate trash. That is our vision. All of this is now possible with the recent advances of deep learning. However, to learn accurate trash detectors, deep learning needs many annotated images. Enter TACO. While there are a few other trash datasets, we believe these are not enough. Our goal is to take TACO to the next level with the following features:

- **Object segmentation**. Typically used bounding boxes are not enough for certain tasks, e.g., robotic grasping

- **Images under free licence**. You can do whatever you want with TACO as long as you cite us.

- **Background annotation**. TACO covers many environments which are tagged for convenience.

- **Object context tag**. Not all objects in TACO are strictly litter. Some objects are handheld or not even trash yet. Thus, objects are tagged based on context

## Team HackATon
<p align="center">
    <img src="readme_res/group_pic.png" width="1200">
</p>

### Team Leader:
- Carl Angelo Pepino

### Members:
- James Ewican
- Karl Phoenix Cornilla
- Rafael Mendoza
- Clark Modequillo
