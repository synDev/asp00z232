package server.content.skills;

import server.Config;
import server.event.*;
import server.game.players.Client;
import server.game.players.PathFinder;

;

/**
 * Class Mining Handles Mining
 *
 * @author 2012 20:16 22/01/2011
 */

public class Agility {
    private Client c;

    public Agility(Client Client) {
        this.c = Client;
    }

    /**
     * * @author L A
     * *
     */

    private boolean logBalance, obstacleNetUp, treeBranchUp, balanceRope, treeBranchDown, obstacleNetOver;
    public static boolean doingAgility;

    public static void agilityWalk(final Client c, final int walkAnimation, final int x, final int y) {
        c.isRunning2 = false;
        c.getPA().sendFrame36(173, 0);
        c.playerWalkIndex = walkAnimation;
        c.getPA().requestUpdates();
        c.getPA().walkToOld(x, y);
    }

    public static void resetAgilityWalk(final Client c) {
        c.isRunning2 = true;
        c.getPA().sendFrame36(173, 1);
        c.playerWalkIndex = 0x333;
        c.getPA().requestUpdates();
    }

    private int[] agilityObject = {
            2295, 5110,
            2285, 2286,
            2313, 5090,
            2312, 5111, 2333, 2334, 2335,
            2314, 2315, 5088, 5099, 5949,
            154, 4058, 9326, 9321
    };

    public boolean agilityObstacle(final Client c, final int objectType) {
        for (int i = 0; i < agilityObject.length; i++) {
            if (objectType == agilityObject[i]) {
                return true;
            }
        }
        return false;
    }

    public void agilityCourse(final Client c, final int objectType) {
        switch (objectType) {
            case 9321:
                if (c.playerLevel[c.playerAgility] >= 53) {
                    if (c.absX == 2735 && c.absY == 10008) {
                        doingAgility = true;
                        c.getPA().movePlayer(2734, 10008, 0);
                        c.startAnimation(752);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.npcId2 = 2003;
                                c.isNpc = true;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 1);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                Agility.agilityWalk(c, 769, -3, 0);
                                c.postProcessing();
                                container.stop();
                            }

                            @Override
                            public void stop() {
                                Agility.resetAgilityWalk(c);
                                Agility.doingAgility = false;
                            }
                        }, 2);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.getPA().movePlayer(2730, 10008, 0);
                                c.isNpc = false;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                                c.startAnimation(758);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                                doingAgility = false;
                            }
                        }, 4);
                    } else if (c.absX == 2730 && c.absY == 10008) {
                        doingAgility = true;
                        c.getPA().movePlayer(2731, 10008, 0);
                        c.startAnimation(752);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.npcId2 = 2003;
                                c.isNpc = true;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 1);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                Agility.agilityWalk(c, 769, 3, 0);
                                c.postProcessing();
                                container.stop();
                            }

                            @Override
                            public void stop() {
                                Agility.resetAgilityWalk(c);
                                Agility.doingAgility = false;
                            }
                        }, 2);
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {// animation
                            @Override
                            public void execute(CycleEventContainer container) {
                                c.getPA().movePlayer(2735, 10008, 0);
                                c.isNpc = false;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                                c.startAnimation(758);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                                doingAgility = false;
                            }
                        }, 4);
                    }
                } else {
                    c.sendMessage("You need an agility level of 56 to squeeze through this.");
                }
                break;
            case 9326:
                if (c.playerLevel[c.playerAgility] >= 53) {
                    doingAgility = true;
                    c.startAnimation(3067);
                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            if (c.absX == 2775 && c.absY == 10003) {
                                c.getPA().movePlayer(2773, 10003, 0);
                                container.stop();
                            } else if (c.absX == 2773 && c.absY == 10003) {
                                c.getPA().movePlayer(2775, 10003, 0);
                                container.stop();
                            } else if (c.absX == 2770 && c.absY == 10002) {
                                c.getPA().movePlayer(2768, 10002, 0);
                                container.stop();
                            } else if (c.absX == 2768 && c.absY == 10002) {
                                c.getPA().movePlayer(2770, 10002, 0);
                                container.stop();
                            }
                        }

                        @Override
                        public void stop() {
                            doingAgility = false;
                            c.getPA().addSkillXP((int) 8.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        }
                    }, 1);
                } else {
                    c.sendMessage("You need an agility level of 53 to jump this.");
                }
                break;
            case 5099:
                doingAgility = true;
                c.startAnimation(749);
                agilityWalk(c, 844, 0, 9);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absY == 9492) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        c.startAnimation(748);
                        resetAgilityWalk(c);
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 5090:
                while (c.absX != 2687 && c.absY != 9506) {
                    c.getPA().walkToOld(2687 - c.absX, 9506 - c.absY);
                }
                agilityWalk(c, 762, -5, 0);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2682 && c.absY == 9506) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 5088:
                while (c.absX != 2682 && c.absY != 9506) {
                    c.getPA().walkToOld(2682 - c.absX, 9506 - c.absY);
                }
                agilityWalk(c, 762, 5, 0);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2687 && c.absY == 9506) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 5949:
                if (c.absX != 3221 && c.absY != 9552) {
                    PathFinder.getPathFinder().findRoute(c, 3221, 9552, true, 3221 - c.absX, 9552 - c.absY);
                }
                if (c.absX == 3221 && c.absY == 9556) {
                    doingAgility = true;
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
                                    agilityWalk(c, 769, 0, -2);
                                    c.postProcessing();
                                    container.stop();
                                }

                                @Override
                                public void stop() {
                                    resetAgilityWalk(c);
                                }
                            }, 1);
                        }
                    }, 2);
                    doingAgility = false;
                } else if (c.absX == 3221 && c.absY == 9552) {
                    doingAgility = true;
                    CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            agilityWalk(c, 769, 0, 2);
                            c.postProcessing();
                            container.stop();
                        }

                        @Override
                        public void stop() {
                            resetAgilityWalk(c);
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
                                        }
                                    }, 1);
                                }
                            }, 2);
                        }
                    }, 1);
                    doingAgility = false;
                }
                break;
            case 5110:
                doingAgility = true;
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2649 && c.absY == 9560) {
                            agilityWalk(c, 769, 0, 2);
                            c.postProcessing();
                        } else if (c.absX == 2649 && c.absY == 9561) {
                            agilityWalk(c, 769, 0, 1);
                            c.postProcessing();
                        } else if (c.absX == 2649 && c.absY == 9562) {
                            agilityWalk(c, 769, 0, -1);
                            c.postProcessing();
                        }
                        container.stop();
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 5111:
                doingAgility = true;
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2647 && c.absY == 9559) {
                            agilityWalk(c, 769, 0, -2);
                            c.postProcessing();
                        } else if (c.absX == 2647 && c.absY == 9557) {
                            agilityWalk(c, 769, 0, 1);
                            c.postProcessing();
                        }
                        container.stop();
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 2333:
                doingAgility = true;
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        c.startAnimation(769);
                        EventManager.getSingleton().addEvent(new Event() {
                            @Override
                            public void execute(EventContainer container) {
                                c.getPA().movePlayer(2925, 2950, 0);
                                container.stop();
                                EventManager.getSingleton().addEvent(new Event() {
                                    @Override
                                    public void execute(EventContainer container) {
                                        c.startAnimation(769);
                                        container.stop();
                                        EventManager.getSingleton().addEvent(new Event() {
                                            @Override
                                            public void execute(EventContainer container) {
                                                c.getPA().movePlayer(2925, 2949, 0);
                                                container.stop();
                                                EventManager.getSingleton().addEvent(new Event() {
                                                    @Override
                                                    public void execute(EventContainer container) {
                                                        c.startAnimation(769);
                                                        container.stop();
                                                        EventManager.getSingleton().addEvent(new Event() {
                                                            @Override
                                                            public void execute(EventContainer container) {
                                                                c.getPA().movePlayer(2925, 2948, 0);
                                                                container.stop();
                                                                EventManager.getSingleton().addEvent(new Event() {
                                                                    @Override
                                                                    public void execute(EventContainer container) {
                                                                        c.startAnimation(769);
                                                                        container.stop();
                                                                        EventManager.getSingleton().addEvent(new Event() {
                                                                            @Override
                                                                            public void execute(EventContainer container) {
                                                                                c.getPA().movePlayer(2925, 2947, 0);
                                                                                container.stop();
                                                                            }
                                                                        }, 430);
                                                                    }
                                                                }, 950);
                                                            }
                                                        }, 390);
                                                    }
                                                }, 950);
                                            }
                                        }, 730);
                                    }
                                }, 950);
                            }
                        }, 730);
                        container.stop();
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        doingAgility = false;
                        c.getPA().addSkillXP((int) 8.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                    }
                }, 1);
                break;

            case 2335:
                doingAgility = true;
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        c.startAnimation(769);
                        EventManager.getSingleton().addEvent(new Event() {
                            @Override
                            public void execute(EventContainer container) {
                                c.getPA().movePlayer(2925, 2948, 0);
                                container.stop();
                                EventManager.getSingleton().addEvent(new Event() {
                                    @Override
                                    public void execute(EventContainer container) {
                                        c.startAnimation(769);
                                        container.stop();
                                        EventManager.getSingleton().addEvent(new Event() {
                                            @Override
                                            public void execute(EventContainer container) {
                                                c.getPA().movePlayer(2925, 2949, 0);
                                                container.stop();
                                                EventManager.getSingleton().addEvent(new Event() {
                                                    @Override
                                                    public void execute(EventContainer container) {
                                                        c.startAnimation(769);
                                                        container.stop();
                                                        EventManager.getSingleton().addEvent(new Event() {
                                                            @Override
                                                            public void execute(EventContainer container) {
                                                                c.getPA().movePlayer(2925, 2950, 0);
                                                                container.stop();
                                                                EventManager.getSingleton().addEvent(new Event() {
                                                                    @Override
                                                                    public void execute(EventContainer container) {
                                                                        c.startAnimation(769);
                                                                        container.stop();
                                                                        EventManager.getSingleton().addEvent(new Event() {
                                                                            @Override
                                                                            public void execute(EventContainer container) {
                                                                                c.getPA().movePlayer(2925, 2951, 0);
                                                                                container.stop();
                                                                            }
                                                                        }, 430);
                                                                    }
                                                                }, 950);
                                                            }
                                                        }, 390);
                                                    }
                                                }, 950);
                                            }
                                        }, 730);
                                    }
                                }, 950);
                            }
                        }, 730);
                        container.stop();
                    }

                    @Override
                    public void stop() {
                        doingAgility = false;
                        c.getPA().addSkillXP((int) 8.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        resetAgilityWalk(c);
                    }
                }, 1);
                break;
            case 2295:
                doingAgility = true;
                while (c.absX != 2474 && c.absY != 3436) {
                    c.getPA().walkToOld(2474 - c.absX, 3436 - c.absY);
                }
                agilityWalk(c, 762, 0, -7);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2474 && c.absY == 3429) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        c.getPA().addSkillXP((int) 7.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        logBalance = true;
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 2285:
                c.startAnimation(828);
                c.getPA().movePlayer(c.absX, 3424, 1);
                c.getPA().addSkillXP((int) 7.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                obstacleNetUp = true;
                break;
            case 2313:
                c.startAnimation(828);
                c.getPA().movePlayer(2473, 3420, 2);
                c.getPA().addSkillXP((int) 5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                treeBranchUp = true;
                break;
            case 2312:
                doingAgility = true;
                while (c.absX != 2477 && c.absY != 3420) {
                    c.getPA().walkToOld(2477 - c.absX, 3420 - c.absY);
                }
                agilityWalk(c, 762, 6, 0);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absX == 2483 && c.absY == 3420) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        resetAgilityWalk(c);
                        c.getPA().addSkillXP((int) 7 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        balanceRope = true;
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 2314:
            case 2315:
                c.startAnimation(828);
                c.getPA().movePlayer(c.absX, c.absY, 0);
                c.getPA().addSkillXP((int) 5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                treeBranchDown = true;
                break;
            case 2286:
                doingAgility = true;
                c.startAnimation(828);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        c.getPA().movePlayer(c.absX, 3427, 0);
                        container.stop();
                    }

                    @Override
                    public void stop() {
                        c.turnPlayerTo(c.absX, 3426);
                        c.getPA().addSkillXP((int) 8 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        obstacleNetOver = true;
                        doingAgility = false;
                    }
                }, 1);
                break;
            case 154:
            case 4058:
                doingAgility = true;
                while (c.absX != 2484 && c.absY != c.objectY - 1) {
                    c.getPA().walkToOld(2484 - c.absX, (c.objectY - 1) - c.absY);
                }
                c.startAnimation(749);
                agilityWalk(c, 844, 0, 7);
                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (c.absY == 3437) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        c.startAnimation(748);
                        resetAgilityWalk(c);
                        if (logBalance && obstacleNetUp && treeBranchUp && balanceRope && treeBranchDown && obstacleNetOver) {
                            c.getPA().addSkillXP((int) 47 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                            c.sendMessage("You have completed the full gnome agility course.");
                        } else {
                            c.getPA().addSkillXP((int) 7 * Config.AGILITY_EXPERIENCE, c.playerAgility);
                        }
                        logBalance = false;
                        obstacleNetUp = false;
                        treeBranchUp = false;
                        balanceRope = false;
                        treeBranchDown = false;
                        obstacleNetOver = false;
                        doingAgility = false;
                    }
                }, 1);
                break;
        }
    }
}