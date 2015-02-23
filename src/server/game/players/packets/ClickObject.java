package server.game.players.packets;

import core.util.Misc;
import server.Config;
import server.content.skills.Agility;
import server.content.skills.Runecrafting;
import server.event.*;
import server.game.objects.doors.Doors;
import server.game.players.Client;
import server.game.players.PacketType;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252,
            THIRD_CLICK = 70;

    @Override
    public void processPacket(final Client c, int packetType, int packetSize) {
        if (c.spinningPlate) {
            return;
        }
        c.clickObjectType = 0;
        c.objectX = 0;
        c.objectId = 0;
        c.objectY = 0;
        c.objectYOffset = 0;
        c.objectXOffset = 0;
        c.lastX = c.getX();
        c.lastY = c.getY();
        c.getPA().resetFollow();
        c.getCombat().resetPlayerAttack();
        c.getPA().requestUpdates();
        switch (packetType) {

            case FIRST_CLICK:
                c.objectX = c.getInStream().readSignedWordBigEndianA();
                c.objectId = c.getInStream().readUnsignedWord();
                c.objectY = c.getInStream().readUnsignedWordA();
                c.objectDistance = 1;
                c.turnPlayerTo(c.objectX, c.objectY);


                if (c.playerRights >= 3) {
                    Misc.println("objectId: " + c.objectId + "  objectDistance: " + c.objectDistance + "  ObjectX: "
                            + c.objectX + "  objectY: " + c.objectY + " Xoff: "
                            + (c.getX() - c.objectX) + " Yoff: "
                            + (c.getY() - c.objectY));
                } else if (c.playerRights == 3) {
                    c.sendMessage("objectId: " + c.objectId + " objectX: "
                            + c.objectX + " objectY: " + c.objectY);
                }
                if (Math.abs(c.getX() - c.objectX) > 25
                        || Math.abs(c.getY() - c.objectY) > 25) {
                    c.resetWalkingQueue();
                    break;
                }
                for (int i = 0; i < Runecrafting.altarID.length; i++) {
                    if (c.objectId == Runecrafting.altarID[i][0] && c.objectX == Runecrafting.altarID[i][1] && c.objectY == Runecrafting.altarID[i][2]) {
                        Runecrafting.craftRunes(c, c.objectId);
                    }
                }
                if (Math.abs(c.getX() - c.objectX) > 25
                        || Math.abs(c.getY() - c.objectY) > 25) {
                    c.resetWalkingQueue();
                    break;
                }
                switch (c.objectId) {
                    case 2491:
                        c.objectDistance = 5;
                        break;
                    case 5949:
                        c.objectDistance = 2;
                        if (c.absY > 9555) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 3221 && c.absY == 9556) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(3221, 9556);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    c.startAnimation(3067);
                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                        @Override
                                        public void execute(CycleEventContainer container) {
                                            container.stop();
                                        }

                                        @Override
                                        public void stop() {
                                            c.getPA().movePlayer(3221, 9554, 0);
                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                                @Override
                                                public void execute(CycleEventContainer container) {
                                                    Agility.agilityWalk(c, 769, 0, -2);
                                                    c.postProcessing();
                                                    container.stop();
                                                }

                                                @Override
                                                public void stop() {
                                                    Agility.resetAgilityWalk(c);
                                                    Agility.doingAgility = false;
                                                }
                                            }, 1);
                                        }
                                    }, 2);
                                }
                            }, 1);
                        } else if (c.absY < 9555) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 3221 && c.absY == 9552) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(3221, 9552);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                        @Override
                                        public void execute(CycleEventContainer container) {
                                            Agility.agilityWalk(c, 769, 0, 2);
                                            c.postProcessing();
                                            container.stop();
                                        }

                                        @Override
                                        public void stop() {
                                            Agility.resetAgilityWalk(c);
                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                                @Override
                                                public void execute(CycleEventContainer container) {
                                                    c.startAnimation(3067);
                                                    container.stop();
                                                }

                                                @Override
                                                public void stop() {
                                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                                        @Override
                                                        public void execute(CycleEventContainer container) {
                                                            container.stop();
                                                        }

                                                        @Override
                                                        public void stop() {
                                                            c.getPA().movePlayer(3221, 9556, 0);
                                                            Agility.doingAgility = false;
                                                        }
                                                    }, 1);
                                                }
                                            }, 2);
                                        }
                                    }, 1);
                                }
                            }, 1);
                        }
                        break;
                    case 12578:
                        if (c.absY < 2973) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 2864 && c.absY == 2971) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(2864, 2971);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    EventManager.getSingleton().addEvent(new Event() {
                                        @Override
                                        public void execute(EventContainer container) {
                                            c.startAnimation(3067);
                                            container.stop();
                                            c.turnPlayerTo(c.objectX, c.objectY);
                                            EventManager.getSingleton().addEvent(new Event() {
                                                @Override
                                                public void execute(EventContainer container) {
                                                    c.getPA().movePlayer(2864, 2973, 0);
                                                    c.startAnimation(751);
                                                    container.stop();
                                                    EventManager.getSingleton().addEvent(new Event() {
                                                        @Override
                                                        public void execute(EventContainer container) {
                                                            c.getPA().movePlayer(2864, 2976, 0);
                                                            c.turnPlayerTo(2864, 2977);
                                                            container.stop();
                                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                                @Override
                                                                public void execute(CycleEventContainer container) {
                                                                    container.stop();
                                                                }

                                                                @Override
                                                                public void stop() {
                                                                    Agility.doingAgility = false;
                                                                    c.getPA().addSkillXP((int) 11 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                                                                }
                                                            }, 1);
                                                        }
                                                    }, 600); // change the timer if u want
                                                }
                                            }, 600); // change the timer if u want
                                        }
                                    }, 200); // change the timer if u want
                                }
                            }, 1);

                        } else if (c.absY > 2973) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 2864 && c.absY == 2976) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(2864, 2976);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    EventManager.getSingleton().addEvent(new Event() {
                                        @Override
                                        public void execute(EventContainer container) {
                                            c.startAnimation(3067);
                                            container.stop();
                                            c.turnPlayerTo(c.objectX, c.objectY);
                                            EventManager.getSingleton().addEvent(new Event() {
                                                @Override
                                                public void execute(EventContainer container) {
                                                    c.getPA().movePlayer(2864, 2975, 0);
                                                    c.startAnimation(751);
                                                    container.stop();
                                                    EventManager.getSingleton().addEvent(new Event() {
                                                        @Override
                                                        public void execute(EventContainer container) {
                                                            c.getPA().movePlayer(2864, 2971, 0);
                                                            c.turnPlayerTo(2864, 2970);
                                                            container.stop();
                                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                                @Override
                                                                public void execute(CycleEventContainer container) {
                                                                    container.stop();
                                                                }

                                                                @Override
                                                                public void stop() {
                                                                    Agility.doingAgility = false;
                                                                    c.getPA().addSkillXP((int) 11 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                                                                }
                                                            }, 1);
                                                        }
                                                    }, 600); // change the timer if u want
                                                }
                                            }, 600); // change the timer if u want
                                        }
                                    }, 200); // change the timer if u want
                                }
                            }, 1);

                        }
                        break;
                    case 3564:
                        if (c.absY < 3215) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 2744 && c.absY == 3211) {
                                        c.turnPlayerTo(2744, 3215);
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(2744, 3211);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    EventManager.getSingleton().addEvent(new Event() {
                                        @Override
                                        public void execute(EventContainer container) {
                                            c.turnPlayerTo(2744, 3215);
                                            c.startAnimation(742);
                                            container.stop();
                                            EventManager.getSingleton().addEvent(new Event() {
                                                @Override
                                                public void execute(EventContainer container) {
                                                    Agility.agilityWalk(c, 744, 0, 8);
                                                    c.postProcessing();
                                                    container.stop();
                                                    EventManager.getSingleton().addEvent(new Event() {
                                                        @Override
                                                        public void execute(EventContainer container) {
                                                            c.startAnimation(743);
                                                            container.stop();
                                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                                @Override
                                                                public void execute(CycleEventContainer container) {
                                                                    //c.getPA().movePlayer(2861, 2957, 0);
                                                                    container.stop();
                                                                }

                                                                @Override
                                                                public void stop() {
                                                                    Agility.doingAgility = false;
                                                                    c.getPA().addSkillXP((int) 12 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                                                                    Agility.resetAgilityWalk(c);
                                                                }
                                                            }, 1);
                                                        }
                                                    }, 5000); // change the timer if u want
                                                }
                                            }, 200); // change the timer if u want
                                        }
                                    }, 1600); // change the timer if u want
                                }
                            }, 1);
                        } else if (c.absY > 3215) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 2744 && c.absY == 3219) {
                                        container.stop();
                                    } else if (c.absX == 2743 && c.absY == 3219) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(2743, 3219);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    EventManager.getSingleton().addEvent(new Event() {
                                        @Override
                                        public void execute(EventContainer container) {
                                            if (c.absX == 2743 && c.absY == 3219) {
                                                c.getPA().walkToOld(1, 0);
                                                c.postProcessing();
                                                container.stop();
                                            } else {
                                                container.stop();
                                            }
                                            EventManager.getSingleton().addEvent(new Event() {
                                                @Override
                                                public void execute(EventContainer container) {
                                                    c.turnPlayerTo(2744, 3215);
                                                    c.startAnimation(742);
                                                    container.stop();
                                                    EventManager.getSingleton().addEvent(new Event() {
                                                        @Override
                                                        public void execute(EventContainer container) {
                                                            Agility.agilityWalk(c, 744, 0, -8);
                                                            c.postProcessing();
                                                            container.stop();
                                                            EventManager.getSingleton().addEvent(new Event() {
                                                                @Override
                                                                public void execute(EventContainer container) {
                                                                    c.startAnimation(743);
                                                                    container.stop();
                                                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                                        @Override
                                                                        public void execute(CycleEventContainer container) {
                                                                            //c.getPA().movePlayer(2861, 2957, 0);
                                                                            container.stop();
                                                                        }

                                                                        @Override
                                                                        public void stop() {
                                                                            Agility.doingAgility = false;
                                                                            c.getPA().addSkillXP((int) 12 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                                                                            Agility.resetAgilityWalk(c);
                                                                        }
                                                                    }, 1);
                                                                }
                                                            }, 5000); // change the timer if u want
                                                        }
                                                    }, 300); // change the timer if u want
                                                }
                                            }, 1600); // change the timer if u want
                                        }
                                    }, 300); // change the timer if u want
                                }
                            }, 1);
                        }
                        break;
                    case 2323:
                        if (c.absX > 2968) {
                            CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    if (c.absX == 2970 && c.absY == 2909) {
                                        container.stop();
                                    } else {
                                        c.getPA().playerWalk(2970, 2909);
                                    }
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    c.startAnimation(3067);
                                    c.turnPlayerTo(c.objectX, c.objectY);
                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                        @Override
                                        public void execute(CycleEventContainer container) {
                                            container.stop();
                                        }

                                        @Override
                                        public void stop() {
                                            c.getPA().movePlayer(2969, 2909, 0);
                                            c.startAnimation(751);
                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                @Override
                                                public void execute(CycleEventContainer container) {
                                                    c.getPA().movePlayer(2965, 2909, 0);
                                                    container.stop();
                                                }

                                                @Override
                                                public void stop() {
                                                    c.turnPlayerTo(2964, 2909);
                                                    Agility.doingAgility = false;
                                                }
                                            }, 1);
                                        }
                                    }, 1);
                                }
                            }, 1);
                        } else if (c.absX < 2968) {
                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                @Override
                                public void execute(CycleEventContainer container) {
                                    container.stop();
                                }

                                @Override
                                public void stop() {
                                    Agility.doingAgility = true;
                                    c.startAnimation(3067);
                                    c.turnPlayerTo(c.objectX, c.objectY);
                                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                        @Override
                                        public void execute(CycleEventContainer container) {
                                            container.stop();
                                        }

                                        @Override
                                        public void stop() {
                                            c.getPA().movePlayer(2968, 2909, 0);
                                            c.startAnimation(751);
                                            CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                                                @Override
                                                public void execute(CycleEventContainer container) {
                                                    c.getPA().movePlayer(2970, 2909, 0);
                                                    container.stop();
                                                }

                                                @Override
                                                public void stop() {
                                                    c.turnPlayerTo(2971, 2909);
                                                    Agility.doingAgility = false;
                                                }
                                            }, 1);
                                        }
                                    }, 1);
                                }
                            }, 1);
                        }
                        break;
                    case 2259:
                    case 2260:
                        c.objectYOffset = -1;
                        //c.objectDistance = 1;
                        break;
                    case 5082:
                        c.objectXOffset = -1;
                        c.objectDistance = 3;
                        break;
                    case 1722:
                        c.objectYOffset = 3;
                        c.objectDistance = 2;
                        break;
                    case 1723:
                        c.objectYOffset = -1;
                        c.objectDistance = 2;
                        break;
                    case 2781:
                        c.objectDistance = 2;
                        break;
                    case 3033:
                        c.objectXOffset = 2;
                        c.objectYOffset = 1;
                        break;
                    case 1733:
                        if (c.objectX == 3058 && c.objectY == 3376) {
                            c.objectXOffset = 3;
                            c.objectYOffset = 0;
                        }
                        break;
                    case 3044:
                        c.objectDistance = 3;
                        break;


                    case 10321:
                        c.objectDistance = 1;
                        CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (c.absX == 2818 && c.absY == 2946) {
                                    container.stop();
                                } else {
                                    c.getPA().playerWalk(2818, 2946);
                                }
                            }

                            @Override
                            public void stop() {
                                Agility.doingAgility = true;
                                c.startAnimation(827);
                                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                                    @Override
                                    public void execute(CycleEventContainer container) {
                                        c.getPA().movePlayer(3021, 9739, 0);
                                        container.stop();
                                    }

                                    @Override
                                    public void stop() {
                                        Agility.doingAgility = false;
                                    }
                                }, 1);
                            }
                        }, 1);
                        break;
                    case 12309:
                        c.objectDistance = 1;
                        CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (c.absX == 2801 && c.absY == 3061) {
                                    container.stop();
                                } else {
                                    c.getPA().playerWalk(2801, 3061);
                                }
                            }

                            @Override
                            public void stop() {
                                c.getPA().openUpBank();
                            }
                        }, 1);
                        break;
                    case 1739:
                    case 1738:
                        if (c.objectY == 3447 && c.objectX == 3144) {
                            c.objectDistance = (int) 1.8;
                            break;
                        }
                        c.objectXOffset = 1;
                        c.objectYOffset = 2;
                        break;
                    case 245:
                        c.objectYOffset = -1;
                        c.objectDistance = 0;
                        break;
                    case 272:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;

                    case 273:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;

                    case 246:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 11666:
                        c.objectDistance = 2;
                        break;
                    case 4493:
                    case 4494:
                    case 4496:
                    case 4495:
                        c.objectDistance = 5;
                        break;
                    case 10229:
                    case 6522:
                        c.objectDistance = 2;
                        break;
                    case 8959:
                        c.objectYOffset = 1;
                        break;
                    case 4417:
                        if (c.objectX == 2425 && c.objectY == 3074)
                            c.objectYOffset = 2;
                        break;
                    case 4420:
                        if (c.getX() >= 2383 && c.getX() <= 2385) {
                            c.objectYOffset = 1;
                        } else {
                            c.objectYOffset = -2;
                        }
                    case 2617:
                        c.objectYOffset = 3;
                        c.objectXOffset = 2;
                        break;
                    case 6552:
                    case 409:
                    case 399:
                    case 398:
                    case 2145:
                    case 2146:
                        c.objectDistance = 2;
                        break;
                    case 2879:
                    case 2878:
                        c.objectDistance = 3;
                        break;
                    case 2558:
                        c.objectDistance = 0;
                        if (c.absX > c.objectX && c.objectX == 3044)
                            c.objectXOffset = 1;
                        if (c.absY > c.objectY)
                            c.objectYOffset = 1;
                        if (c.absX < c.objectX && c.objectX == 3038)
                            c.objectXOffset = -1;
                        break;


                    case 9356:
                        c.objectDistance = 2;
                        break;
                    case 5959:
                    case 1815:
                    case 5960:
                    case 1816:
                        c.objectDistance = 0;
                        break;

                    case 9293:
                        c.objectDistance = 2;
                        break;
                    case 4418:
                        if (c.objectX == 2374 && c.objectY == 3131)
                            c.objectYOffset = -2;
                        else if (c.objectX == 2369 && c.objectY == 3126)
                            c.objectXOffset = 2;
                        else if (c.objectX == 2380 && c.objectY == 3127)
                            c.objectYOffset = 2;
                        else if (c.objectX == 2369 && c.objectY == 3126)
                            c.objectXOffset = 2;
                        else if (c.objectX == 2374 && c.objectY == 3131)
                            c.objectYOffset = -2;
                        break;
                    case 9706:
                        c.objectDistance = 0;
                        c.objectXOffset = 1;
                        break;
                    case 9707:
                        c.objectDistance = 0;
                        c.objectYOffset = -1;
                        break;
                    case 4419:
                    case 6707: // verac
                        c.objectYOffset = 3;
                        break;
                    case 2609:
                        c.objectYOffset = 2;
                        break;
                    case 6823:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 12536: // wizard stairs
                        c.objectXOffset = 2;
                        c.objectYOffset = 1;
                        break;
                    case 12537: // wizard stairs
                        c.objectXOffset = 1;
                        c.objectYOffset = 2;
                        break;
                    case 6773:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 6821:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 1276:
                    case 3879:
                    case 3883:
                    case 1278:// trees
                    case 1281: // oak
                    case 1308: // willow
                    case 1307: // maple
                    case 1309: // yew
                    case 1306: // yew
                    case 5551: // willow
                    case 5553: // willow
                    case 5552:// willow
                        c.objectDistance = 3;
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
                        + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
                    if (Doors.getSingleton().handleDoor(c, c.objectId, c.objectX,
                            c.objectY, c.heightLevel)) {
                    }
                    c.getActions().firstClickObject(c.objectId, c.objectX,
                            c.objectY);

                } else {
                    c.clickObjectType = 1;
                    c.walkingtoObject = true;
                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (!c.walkingtoObject)
                                container.stop();
                            if (c.goodDistance(c.objectX + c.objectXOffset,
                                    c.objectY + c.objectYOffset, c.getX(),
                                    c.getY(), c.objectDistance)) {
                                if (Doors.getSingleton().handleDoor(c, c.objectId, c.objectX,
                                        c.objectY, c.heightLevel)) {
                                }
                                c.getActions().firstClickObject(c.objectId,
                                        c.objectX, c.objectY);

                                container.stop();
                            }

                        }

                        @Override
                        public void stop() {
                            c.walkingtoObject = false;
                        }
                    }, 1);
                }
                break;

            case SECOND_CLICK:
                c.objectId = c.getInStream().readUnsignedWordBigEndianA();
                c.objectY = c.getInStream().readSignedWordBigEndian();
                c.objectX = c.getInStream().readUnsignedWordA();
                c.objectDistance = 1;
                c.turnPlayerTo(c.objectX, c.objectY);
                if (c.playerRights >= 3) {
                    Misc.println("objectId: " + c.objectId + "objectDistance: " + c.objectDistance + "  ObjectX: "
                            + c.objectX + "  objectY: " + c.objectY + " Xoff: "
                            + (c.getX() - c.objectX) + " Yoff: "
                            + (c.getY() - c.objectY));
                }

                switch (c.objectId) {
                    case 12309:
                        c.objectDistance = 1;
                        CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (c.absX == 2801 && c.absY == 3061) {
                                    container.stop();
                                } else {
                                    c.getPA().playerWalk(2801, 3061);
                                }
                            }

                            @Override
                            public void stop() {
                                c.getShops().openShop(108);
                            }
                        }, 1);
                        break;
                    case 2781:
                        c.objectDistance = 2;
                        break;
                    case 2491:
                        c.objectDistance = 5;
                        break;
                    case 6163:
                    case 6165:
                    case 6166:
                    case 6164:
                    case 6162:
                        c.objectDistance = 2;
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;

                }
                if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
                        + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
                    c.getActions().secondClickObject(c.objectId, c.objectX,
                            c.objectY);
                } else {
                    c.clickObjectType = 2;
                    c.clickObjectType = 1;
                    c.walkingtoObject = true;
                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (!c.walkingtoObject)
                                container.stop();
                            if (c.goodDistance(c.objectX + c.objectXOffset,
                                    c.objectY + c.objectYOffset, c.getX(),
                                    c.getY(), c.objectDistance)) {
                                c.getActions().secondClickObject(c.objectId,
                                        c.objectX, c.objectY);

                                container.stop();
                            }

                        }

                        @Override
                        public void stop() {
                            c.walkingtoObject = false;
                        }
                    }, 1);
                }
                break;

            case THIRD_CLICK:
                c.turnPlayerTo(c.objectX, c.objectY);
                c.objectX = c.getInStream().readSignedWordBigEndian();
                c.objectY = c.getInStream().readUnsignedWord();
                c.objectId = c.getInStream().readUnsignedWordBigEndianA();

                if (c.playerRights >= 3) {
                    Misc.println("objectId: " + c.objectId + "  ObjectX: "
                            + c.objectX + "  objectY: " + c.objectY + " Xoff: "
                            + (c.getX() - c.objectX) + " Yoff: "
                            + (c.getY() - c.objectY));
                }

                switch (c.objectId) {
                    case 12309:
                        c.objectDistance = 1;
                        CycleEventHandler.getSingleton().addEvent(18, c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (c.absX == 2801 && c.absY == 3061) {
                                    container.stop();
                                } else {
                                    c.getPA().playerWalk(2801, 3061);
                                }
                            }

                            @Override
                            public void stop() {
                                c.getShops().openShop(14);
                            }
                        }, 1);
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
                        + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
                    c.getActions().secondClickObject(c.objectId, c.objectX,
                            c.objectY);
                } else {
                    c.clickObjectType = 3;
                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (c.clickObjectType == 3
                                    && c.goodDistance(c.objectX + c.objectXOffset,
                                    c.objectY + c.objectYOffset, c.getX(),
                                    c.getY(), c.objectDistance)) {
                                c.getActions().thirdClickObject(c.objectId,
                                        c.objectX, c.objectY);
                                container.stop();
                            }
                            if (c.clickObjectType < 3)
                                container.stop();
                        }

                        @Override
                        public void stop() {
                            c.clickObjectType = 0;
                        }
                    }, 1);
                }
                break;
        }

    }

    public void handleSpecialCase(Client c, int id, int x, int y) {

    }

}

